package org.spicefactory.parsley.core.messaging.impl;

import java.util.HashMap;
import java.util.Map;

import org.spicefactory.parsley.core.messaging.MessageReceiverRegistry;
import org.spicefactory.parsley.core.messaging.receiver.CommandObserver;
import org.spicefactory.parsley.core.messaging.receiver.MessageExceptionHandler;
import org.spicefactory.parsley.core.messaging.receiver.MessageTarget;

/**
 * Guice implementation of the MessageReceiverRegistry interface.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
class GuiceMessageReceiverRegistry implements MessageReceiverRegistry {

	private final Map<Class<?>, MessageReceiverCollection> receivers;
	private final Map<Class<?>, DefaultMessageReceiverCache> selectionCache;

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	GuiceMessageReceiverRegistry() {
		// TODO: Check maps implementation + concurrency.
		receivers = new HashMap<Class<?>, MessageReceiverCollection>();
		selectionCache = new HashMap<Class<?>, DefaultMessageReceiverCache>();
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	@Override
	public void addTarget(MessageTarget target) {
		// TODO Auto-generated method stub
		System.err.println("GuiceMessageReceiverRegistry.addTarget: " + target);
	}

	@Override
	public void removeTarget(MessageTarget target) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addExceptionHandler(MessageExceptionHandler handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeErrorHandler(MessageExceptionHandler handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addCommandObserver(CommandObserver observer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeCommandObserver(CommandObserver observer) {
		// TODO Auto-generated method stub

	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////
}
