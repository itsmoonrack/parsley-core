package org.spicefactory.parsley.core.messaging;

/**
 * Represents a single message and all its relevant settings.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface Message<T> {

	/**
	 * Returns actual message instance.
	 */
	T getInstance();

	/**
	 * Returns type of the message.
	 */
	Class<? extends T> getType();

	/**
	 * Returns selector to use to determine matching receivers.
	 */
	int getSelector();

}
