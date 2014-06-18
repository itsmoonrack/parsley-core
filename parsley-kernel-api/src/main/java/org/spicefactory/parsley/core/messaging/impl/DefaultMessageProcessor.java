package org.spicefactory.parsley.core.messaging.impl;

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
import org.spicefactory.parsley.core.messaging.MessageState;
import org.spicefactory.parsley.core.messaging.receiver.MessageReceiver;
import org.spicefactory.parsley.core.messaging.receiver.MessageTarget;

//Package-private.
final class DefaultMessageProcessor implements MessageProcessor {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private Processor currentProcessor;
	private List<Processor> remainingProcessors;
	private MessageState state;

	private final Message message;
	private final MessageReceiverCache cache;
	private final Method receiverHandler;

	// Package-private.
	DefaultMessageProcessor(Message message, MessageReceiverCache cache) {
		this(message, cache, null);
	}

	// Package-private.
	DefaultMessageProcessor(Message message, MessageReceiverCache cache, @Nullable Method receiverHandler) {
		this.cache = cache;
		this.message = message;
		this.receiverHandler = receiverHandler;
	}

	public void proceed() {
		if (state == null) {
			start();
		} else {
			resume();
		}
	}

	@Override
	public Message message() {
		// TODO Auto-generated method stub
		return null;

	}

	@Override
	public MessageState state() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void suspend() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		if (state != MessageState.SUSPENDED) {
			throw new IllegalStateException("Cannot resume. Message state not suspended.");
		}
		state = MessageState.ACTIVE;
		processReceivers();
	}

	public void start() {

	}

	@Override
	public void rewind() {
		if (state == MessageState.CANCELLED) {
			throw new IllegalStateException("Cannot rewind. Message state is cancelled.");
		}
		logger.trace("Rewind message '{}' with {} receiver(s).", message.type(), remainingProcessors.get(0).receiverCount());
		createProcessors();
	}

	private void createProcessors() {
		currentProcessor = new Processor(fetchReceivers(), receiverHandler);
		remainingProcessors = new ArrayList<DefaultMessageProcessor.Processor>();
	}

	/**
	 * Fetches the receivers for the message type and receiver kind this processor handles.
	 * @return the receivers for the message type and receiver kind this processor handles
	 */
	protected List<MessageReceiver> fetchReceivers() {
		return new ArrayList<MessageReceiver>(cache.getReceivers(MessageReceiverKind.TARGET, message.selector()));
	}

	@Override
	public void sendResponse(Object message, Object selector) {

	}

	private void invokeTarget(MessageTarget target) {
		target.handleMessage(this);
	}

	class Processor implements Runnable {

		private volatile int currentIndex = 0;

		private final List<MessageReceiver> receivers;
		private final boolean handleErrors;
		private final Method handler;

		Processor(List<MessageReceiver> receivers, Method handler) {
			this(receivers, handler, true);
		}

		Processor(List<MessageReceiver> receivers, Method handler, boolean handleErrors) {
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

		@Override
		public void run() {
			do {
				while (en.hasMoreElements()) {

				}
			}
			while (state == MessageState.ACTIVE);

			final MessageReceiver receiver = receivers.get(currentIndex++);
			if (handler != null) {
				handler.invoke(receiver, this);
			} else {
				invokeTarget((MessageTarget) receiver);
			}
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
}
