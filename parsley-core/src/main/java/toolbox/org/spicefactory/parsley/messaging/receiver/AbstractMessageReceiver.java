package org.spicefactory.parsley.messaging.receiver;

import org.spicefactory.parsley.core.messaging.receiver.MessageReceiver;
import org.spicefactory.parsley.messaging.annotation.MessageHandler;

public abstract class AbstractMessageReceiver implements MessageReceiver {

	/**
	 * The receiver configuration in three (mutable) model object.
	 */
	protected Class<?> type;
	protected String selector;
	protected int order;

	public AbstractMessageReceiver(MessageHandler info) {
		this.type = info.type();
		this.selector = info.selector();
		this.order = info.order();
	}

	@Override
	public Class<?> type() {
		return type;
	}

	@Override
	public String selector() {
		return selector;
	}

	@Override
	public int order() {
		return order;
	}

}
