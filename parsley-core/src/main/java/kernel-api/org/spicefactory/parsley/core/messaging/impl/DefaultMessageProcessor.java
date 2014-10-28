package org.spicefactory.parsley.core.messaging.impl;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
class DefaultMessageProcessor implements MessageProcessor {

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	protected DefaultMessageProcessor(Message message, MessageReceiverCache cache, MessageSettings settings) {
		this.cache = cache;
		this.message = message;
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
	public Message message() {
		return message;
	}

	@Override
	public MessageState state() {
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
		setState(state(), "Rewind");
		createProcessors();
	}

	@Override
	public void sendResponse(Object message, Object selector) {
		// TODO: Implement me!
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	private Logger logger = LoggerFactory.getLogger(getClass());

	private Processor currentProcessor;
	private List<Processor> remainingProcessors;
	private MessageState state;

	protected final Message message;
	protected final MessageReceiverCache cache;

	private void createProcessors() {
		currentProcessor = new Processor(fetchReceivers());
		remainingProcessors = new ArrayList<DefaultMessageProcessor.Processor>();
	}

	/**
	 * Fetches the receivers for the message type and receiver kind this processor handles.
	 * @return the receivers for the message type and receiver kind this processor handles
	 */
	protected List<MessageReceiver> fetchReceivers() {
		return cache.getReceivers(MessageReceiverKind.TARGET, message.selector());
	}

	protected void invokeReceiver(MessageReceiver target) {
		((MessageTarget) target).handleMessage(this);
	}

	protected String getTraceString(String status, int receiverCount) {
		return MessageFormat.format("{0} message '{1}' with {2} receiver(s).", status, message.type(), receiverCount);
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

	class Processor {

		private volatile int currentIndex = 0;

		private final List<MessageReceiver> receivers;
		private final boolean handleErrors;

		Processor(List<MessageReceiver> receivers) {
			this(receivers, true);
		}

		Processor(List<MessageReceiver> receivers, boolean handleErrors) {
			Collections.sort(receivers, new MessageSorter());
			this.handleErrors = handleErrors;
			this.receivers = receivers;
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
			invokeReceiver(receivers.get(currentIndex++));
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
