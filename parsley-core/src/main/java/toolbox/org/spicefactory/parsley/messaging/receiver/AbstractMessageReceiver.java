package org.spicefactory.parsley.messaging.receiver;

import org.spicefactory.parsley.core.messaging.receiver.MessageReceiver;

/**
 * Abstract base class for all types of message receivers.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public abstract class AbstractMessageReceiver implements MessageReceiver {

	/**
	 * The receiver configuration in three (mutable) model object.
	 */
	protected Class<?> type;
	protected Object selector;
	protected int order;

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	public AbstractMessageReceiver(MessageReceiverInfo info) {
		this.type = info.type;
		this.selector = info.selector;
		this.order = info.order;
	}

	@Override
	public Class<?> getType() {
		return type;
	}

	@Override
	public Object getSelector() {
		return selector;
	}

	@Override
	public int getOrder() {
		return order;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

}
