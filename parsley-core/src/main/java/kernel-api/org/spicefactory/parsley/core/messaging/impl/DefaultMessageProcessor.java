package org.spicefactory.parsley.core.messaging.impl;

import static org.spicefactory.parsley.core.messaging.MessageReceiverKind.EXCEPTION_HANDLER;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spicefactory.parsley.core.messaging.ExceptionPolicy;
import org.spicefactory.parsley.core.messaging.Message;
import org.spicefactory.parsley.core.messaging.MessageProcessor;
import org.spicefactory.parsley.core.messaging.MessageReceiverCache;
import org.spicefactory.parsley.core.messaging.MessageReceiverKind;
import org.spicefactory.parsley.core.messaging.MessageSettings;
import org.spicefactory.parsley.core.messaging.MessageState;
import org.spicefactory.parsley.core.messaging.receiver.MessageExceptionHandler;
import org.spicefactory.parsley.core.messaging.receiver.MessageReceiver;
import org.spicefactory.parsley.core.messaging.receiver.MessageTarget;

/**
 * Default implementation of MessageProcessor interface.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public class DefaultMessageProcessor implements MessageProcessor<Object> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	protected final Message<Object> message;
	protected final MessageReceiverCache cache;
	protected final MessageSettings settings;
	protected final Handler receiverHandler;

	private Processor currentProcessor;
	private Throwable currentThrowable;
	private List<Processor> remainingProcessors;
	private MessageState state;

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	protected DefaultMessageProcessor(Message<Object> message, MessageReceiverCache cache, MessageSettings settings) {
		this(message, cache, settings, null);
	}

	protected DefaultMessageProcessor(Message<Object> message, MessageReceiverCache cache, MessageSettings settings,
			@Nullable Handler receiverHandler) {
		this.cache = cache;
		this.message = message;
		this.settings = settings;
		this.receiverHandler = receiverHandler != null ? receiverHandler : invokeTarget;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	public void proceed() {
		if (state == null) {
			start();
		} else {
			resume();
		}
	}

	@Override
	public Message<Object> getMessage() {
		return message;
	}

	@Override
	public MessageState getState() {
		return state;
	}

	@Override
	public void cancel() {
		if (state == MessageState.CANCELLED) {
			return;
		}
		setState(MessageState.CANCELLED, "Cancel");
		currentProcessor = null;
		remainingProcessors = null;
	}

	@Override
	public void suspend() {
		if (state != MessageState.ACTIVE) {
			throw new IllegalStateException("Cannot suspend. Message state not active.");
		}
		setState(MessageState.SUSPENDED, "Suspend");
	}

	@Override
	public void resume() {
		if (state != MessageState.SUSPENDED) {
			throw new IllegalStateException("Cannot resume. Message state not suspended.");
		}
		setState(MessageState.ACTIVE, "Resume");
		processReceivers();
	}

	public void start() {
		createProcessors();
		setState(MessageState.ACTIVE, "Dispatch");
		processReceivers();
	}

	@Override
	public void rewind() {
		if (state == MessageState.CANCELLED) {
			throw new IllegalStateException("Cannot rewind. Message state is cancelled.");
		}
		setState(getState(), "Rewind");
		createProcessors();
	}

	@Override
	public void sendResponse(Object message, Object selector) {
		// TODO: Implement me!
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	private void createProcessors() {
		currentProcessor = new Processor(fetchReceivers(), receiverHandler);
		remainingProcessors = new ArrayList<Processor>();
	}

	/**
	 * Fetches the receivers for the message type and receiver kind this processor handles.
	 * @return the receivers for the message type and receiver kind this processor handles
	 */
	protected List<MessageReceiver> fetchReceivers() {
		return cache.getReceivers(MessageReceiverKind.TARGET, message.getSelector());
	}

	protected String getTraceString(String status, int receiverCount) {
		return MessageFormat.format("{0} message {1} with {2} receiver(s).", status, message.getType(), receiverCount);
	}

	/**
	 * Sets processor state and logs a status.
	 * @param newState
	 * @param status
	 */
	private void setState(MessageState newState, String status) {
		if (logger.isTraceEnabled()) {
			int count = currentProcessor.receiverCount();
			if (remainingProcessors.size() > 0) {
				count += remainingProcessors.get(0).receiverCount();
			}
			logger.trace(getTraceString(status, count));
		}

		state = newState;
	}

	private void processReceivers() {
		do {
			while (!currentProcessor.hasNext()) {
				if (complete()) {
					return;
				}
				currentProcessor = remainingProcessors.remove(0);
			}
			try {
				currentProcessor.proceed();
			}
			catch (Throwable e) {
				if (!currentProcessor.handleExceptions || e.getCause() instanceof MessageProcessorException) {
					// Avoid the risk of endless loops.
					throw new RuntimeException(e.getCause());
				}
				if (e.getCause() instanceof InvocationTargetException) {
					e = e.getCause();
				}
				logger.warn("Message receiver {} threw Exception: {}", currentProcessor.currentReceiver(), e.getCause());
				if (!handleException(e.getCause())) {
					return;
				}
			}
		}
		while (state == MessageState.ACTIVE);
	}

	private boolean complete() {
		if (remainingProcessors.isEmpty()) {
			setState(MessageState.COMPLETE, "Complete");
			currentProcessor = null;
			return true;
		}
		return false;
	}

	private boolean handleException(Throwable e) {
		List<MessageExceptionHandler> handlers = new ArrayList<MessageExceptionHandler>();
		List<MessageReceiver> exceptionHandlers = message != null ? //
				cache.getReceivers(EXCEPTION_HANDLER, message.getSelector()) : new ArrayList<MessageReceiver>();

		for (MessageReceiver h : exceptionHandlers) {
			MessageExceptionHandler exceptionHandler = (MessageExceptionHandler) h;
			if (exceptionHandler.getExceptionType().isAssignableFrom(e.getClass())) {
				handlers.add(exceptionHandler);
			}
		}

		if (logger.isInfoEnabled()) {
			logger.info("Select {} out of {} exception handlers.", handlers.size(), exceptionHandlers.size());
		}

		if (handlers.size() > 0) {
			currentThrowable = e;
			remainingProcessors.add(0, currentProcessor);
			currentProcessor = new Processor(handlers, invokeExceptionHandler, false);
			return true;
		} else {
			return unhandledException(e);
		}
	}

	private boolean unhandledException(Throwable e) {
		if (settings.unhandledException() == ExceptionPolicy.RETHROW) {
			throw new MessageProcessorException("Exception in message receiver", e);
		} else if (settings.unhandledException() == ExceptionPolicy.ABORT) {
			logger.info("Unhandled exception: abort message processor.");
			return false;
		} else {
			logger.info("Unhandled exception: continue message processing.");
			return true;
		}
	}

	// Java 1.8 forward compatibility.
	private final Handler invokeTarget = new Handler() {
		@Override
		public void proceed(MessageProcessor<Object> processor, MessageReceiver target) {
			((MessageTarget) target).handleMessage(processor);
		}
	};

	// Java 1.8 forward compatibility.
	private final Handler invokeExceptionHandler = new Handler() {
		@Override
		public void proceed(MessageProcessor<Object> processor, MessageReceiver target) {
			((MessageExceptionHandler) target).handleException(processor, currentThrowable);
		}
	};

	// @FunctionalInterface
	public static interface Handler {
		void proceed(MessageProcessor<Object> processor, MessageReceiver receiver);
	}

	class Processor {

		private volatile int currentIndex = 0;

		private final List<? extends MessageReceiver> receivers;
		private final Handler handler;
		private final boolean handleExceptions;

		Processor(List<? extends MessageReceiver> receivers, Handler handler) {
			this(receivers, handler, true);
		}

		Processor(List<? extends MessageReceiver> receivers, Handler handler, boolean handleExceptions) {
			Collections.sort(receivers, new MessageSorter());
			this.handleExceptions = handleExceptions;
			this.receivers = receivers;
			this.handler = handler;
		}

		boolean hasNext() {
			return receivers.size() > currentIndex;
		}

		int receiverCount() {
			return receivers.size();
		}

		void rewind() {
			currentIndex = 0;
		}

		MessageReceiver currentReceiver() {
			return hasNext() ? receivers.get(currentIndex) : null;
		}

		void proceed() {
			handler.proceed(DefaultMessageProcessor.this, receivers.get(currentIndex++));
		}
	}

	class MessageSorter implements Comparator<MessageReceiver> {

		@Override
		public int compare(MessageReceiver a, MessageReceiver b) {
			if (a.getOrder() > b.getOrder()) {
				return 1;
			} else if (a.getOrder() < b.getOrder()) {
				return -1;
			}
			return 0;
		}

	}

	class MessageProcessorException extends RuntimeException {
		public MessageProcessorException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
