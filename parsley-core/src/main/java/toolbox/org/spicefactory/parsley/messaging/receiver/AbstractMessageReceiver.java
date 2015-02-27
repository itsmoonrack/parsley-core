package org.spicefactory.parsley.messaging.receiver;

import org.spicefactory.parsley.core.messaging.receiver.MessageReceiver;

/**
 * Abstract base class for all types of message receivers.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public abstract class AbstractMessageReceiver implements MessageReceiver {

	/**
	 * The receiver configuration.
	 */
	protected final MessageReceiverInfo info;

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	public AbstractMessageReceiver(MessageReceiverInfo info) {
		this.info = info;
	}

	@Override
	public Class<?> getType() {
		return info.type;
	}

	@Override
	public Object getSelector() {
		return info.selector;
	}

	@Override
	public int getOrder() {
		return info.order;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

}
