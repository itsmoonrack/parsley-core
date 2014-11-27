package org.spicefactory.parsley.messaging.receiver;

import org.spicefactory.parsley.core.messaging.receiver.MessageReceiver;

public abstract class AbstractMessageReceiver implements MessageReceiver {

	/**
	 * The receiver configuration in three (mutable) model object.
	 */
	protected Class<?> type;
	protected int selector;
	protected int order;

	public AbstractMessageReceiver(MessageReceiverInfo info) {
		this.type = info.type;
		this.selector = info.selector;
		this.order = info.order;
	}

	@Override
	public Class<?> type() {
		return type;
	}

	@Override
	public int selector() {
		return selector;
	}

	@Override
	public int order() {
		return order;
	}

}
