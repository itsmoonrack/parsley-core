package org.spicefactory.parsley.core.messaging.impl;

import java.awt.EventQueue;

import org.spicefactory.parsley.core.messaging.Message;
import org.spicefactory.parsley.core.messaging.MessageProcessor;
import org.spicefactory.parsley.core.messaging.MessageReceiverCache;
import org.spicefactory.parsley.core.messaging.MessageSettings;
import org.spicefactory.parsley.core.messaging.receiver.MessageReceiver;
import org.spicefactory.parsley.core.messaging.receiver.MessageTarget;

class EventQueueMessageProcessor extends DefaultMessageProcessor {


	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	EventQueueMessageProcessor(Message<Object> message, MessageReceiverCache cache, MessageSettings settings) {
		super(message, cache, settings, invokeReceiver);
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	// Java 1.8 forward-compatibility.
	private static final Handler invokeReceiver = new Handler() {
		@Override
		public void proceed(MessageProcessor<Object> processor, MessageReceiver receiver) {
			invokeReceiver(processor, (MessageTarget) receiver);
		}
	};

	private static void invokeReceiver(final MessageProcessor<Object> processor, final MessageTarget target) {
		// Processes the message in the Event Dispatch Thread.
		if (EventQueue.isDispatchThread()) {
			target.handleMessage(processor);
		} else {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					target.handleMessage(processor);
				}
			});
		}
	}
}
