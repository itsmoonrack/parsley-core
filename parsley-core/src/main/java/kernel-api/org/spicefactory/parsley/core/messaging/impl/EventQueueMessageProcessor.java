package org.spicefactory.parsley.core.messaging.impl;

import java.awt.EventQueue;

import org.spicefactory.parsley.core.messaging.Message;
import org.spicefactory.parsley.core.messaging.MessageReceiverCache;
import org.spicefactory.parsley.core.messaging.MessageSettings;
import org.spicefactory.parsley.core.messaging.receiver.MessageReceiver;
import org.spicefactory.parsley.core.messaging.receiver.MessageTarget;

class EventQueueMessageProcessor extends DefaultMessageProcessor {

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	EventQueueMessageProcessor(Message message, MessageReceiverCache cache, MessageSettings settings) {
		super(message, cache, settings);
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	@Override
	protected void invokeReceiver(final MessageReceiver target) {
		// Processes the message in the Event Dispatch Thread.
		if (EventQueue.isDispatchThread()) {
			((MessageTarget) target).handleMessage(this);
		} else {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					((MessageTarget) target).handleMessage(EventQueueMessageProcessor.this);
				}
			});
		}
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////
}
