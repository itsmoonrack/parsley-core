package org.spicefactory.parsley.core.messaging.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spicefactory.parsley.core.messaging.Message;
import org.spicefactory.parsley.core.messaging.MessageProcessor;
import org.spicefactory.parsley.core.messaging.MessageReceiverCache;
import org.spicefactory.parsley.core.messaging.MessageReceiverKind;
import org.spicefactory.parsley.core.messaging.MessageSettings;
import org.spicefactory.parsley.core.messaging.MessageState;
import org.spicefactory.parsley.core.messaging.receiver.MessageReceiver;
import org.spicefactory.parsley.core.messaging.receiver.MessageTarget;

/**
 * Default implementation of MessageProcessor interface.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
//Package-private.
class DefaultMessageProcessor implements MessageProcessor {

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	DefaultMessageProcessor(Message message, MessageReceiverCache cache, MessageSettings settings) {
		this(message, cache, settings, null);
	}

	// Package-private.
	DefaultMessageProcessor(Message message, MessageReceiverCache cache, MessageSettings settings,
			@Nullable MessageReceiverHandler receiverHandler) {
		this.cache = cache;
		this.message = message;
		this.receiverHandler = receiverHandler == null ? new MessageReceiverHandlerInternal() : receiverHandler;
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
	public Message getMessage() {
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

	}

	public final class MessageReceiverHandler extends MessageReceiverHandlerInternal {

		private final Object obj;
		private final Method method;

		public MessageReceiverHandler(Object obj, Method method) {
			this.obj = obj;
			this.method = method;
		}

		@Override
		public void invoke(Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			method.invoke(obj, args);
		}

	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	private Logger logger = LoggerFactory.getLogger(getClass());

	private Processor currentProcessor;
	private List<Processor> remainingProcessors;
	private MessageState state;

	private final Message message;
	private final MessageReceiverCache cache;
	private final MessageReceiverHandlerInternal receiverHandler;

	private void createProcessors() {
		currentProcessor = new Processor(fetchReceivers(), receiverHandler);
		remainingProcessors = new ArrayList<DefaultMessageProcessor.Processor>();
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
			catch (Exception e) {
				if (!currentProcessor.handleErrors || e instanceof MessageProcessorException) {
					// Avoid the risk of endless loops.
					throw new RuntimeException(e);
				}
				logger.warn("Message receiver {} threw Exception: {}", currentProcessor.currentReceiver(), e);
				if (!handleException(e)) {
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

	private boolean handleException(Exception e) {
		// TODO: Implement me.
		return true;
	}

	/**
	 * Fetches the receivers for the message type and receiver kind this processor handles.
	 * @return the receivers for the message type and receiver kind this processor handles
	 */
	protected List<MessageReceiver> fetchReceivers() {
		return new ArrayList<MessageReceiver>(cache.getReceivers(MessageReceiverKind.TARGET, message.selector()));
	}

	/**
	 * Internal handler used to pass this processor on a message target (without using reflection).
	 */
	private class MessageReceiverHandlerInternal {
		public void invoke(Object... args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
			invokeTarget((MessageTarget) args[0]);
		}
	}

	private void invokeTarget(MessageTarget target) {
		target.handleMessage(this);
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
			logger.trace("{} message '{}' with {} receiver(s).", status, message.type(), count);
		}

		state = newState;
	}

	class Processor {

		private volatile int currentIndex = 0;

		private final List<MessageReceiver> receivers;
		private final boolean handleErrors;
		private final MessageReceiverHandlerInternal handler;

		Processor(List<MessageReceiver> receivers, MessageReceiverHandlerInternal handler) {
			this(receivers, handler, true);
		}

		Processor(List<MessageReceiver> receivers, MessageReceiverHandlerInternal handler, boolean handleErrors) {
			Collections.sort(receivers, new MessageSorter());
			this.handleErrors = handleErrors;
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

		void proceed() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
			handler.invoke(receivers.get(currentIndex++));
		}
	}

	class MessageSorter implements Comparator<MessageReceiver> {

		@Override
		public int compare(MessageReceiver a, MessageReceiver b) {
			if (a.order() > b.order()) {
				return 1;
			} else if (a.order() < b.order()) {
				return -1;
			}
			return 0;
		}

	}

	class MessageProcessorException extends RuntimeException {
		/**
		 *
		 */
		private static final long serialVersionUID = 2595177901374008347L;
	}
}
