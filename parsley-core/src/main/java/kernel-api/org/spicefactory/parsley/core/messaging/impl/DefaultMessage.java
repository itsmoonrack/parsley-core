package org.spicefactory.parsley.core.messaging.impl;

import org.spicefactory.parsley.core.messaging.Message;
import org.spicefactory.parsley.core.messaging.Selector;

/**
 * Default implementation of the Message interface.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public final class DefaultMessage implements Message<Object> {

	private final Object instance;
	private final Class<?> type;
	private final int selector;

	public DefaultMessage(Object instance, Class<?> type) {
		this(instance, type, Selector.NONE);
	}

	public DefaultMessage(Object instance, Class<?> type, int selector) {
		this.instance = instance;
		this.type = type;
		this.selector = selector;
	}

	@Override
	public Object getInstance() {
		return instance;
	}

	@Override
	public Class<?> getType() {
		return type;
	}

	@Override
	public int getSelector() {
		return selector;
	}

}
