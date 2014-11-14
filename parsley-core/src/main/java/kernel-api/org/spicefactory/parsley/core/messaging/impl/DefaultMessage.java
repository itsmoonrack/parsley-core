package org.spicefactory.parsley.core.messaging.impl;

import javax.annotation.Nullable;

import org.spicefactory.parsley.core.messaging.Message;

/**
 * Default implementation of the Message interface.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public final class DefaultMessage implements Message {

	private final Object instance;
	private final Class<?> type;
	private final String selector;

	public DefaultMessage(Object instance, Class<?> type) {
		this(instance, type, null);
	}

	public DefaultMessage(Object instance, Class<?> type, @Nullable String selector) {
		this.instance = instance;
		this.type = type;
		this.selector = selector;
	}

	@Override
	public Object getInstance() {
		return instance;
	}

	@Override
	public Class<?> type() {
		return type;
	}

	@Override
	public String selector() {
		return selector;
	}

}
