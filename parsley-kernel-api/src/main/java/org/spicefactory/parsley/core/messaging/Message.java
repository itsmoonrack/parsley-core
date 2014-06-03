package org.spicefactory.parsley.core.messaging;

import javax.annotation.Nullable;

/**
 * Represents a single message and all its relevant settings.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface Message {

	/**
	 * Returns actual message instance.
	 */
	Object instance();

	/**
	 * Returns type of the message.
	 */
	Class<?> type();

	/**
	 * Returns selector to use to determine matching receivers.
	 */
	@Nullable
	Object selector();

}
