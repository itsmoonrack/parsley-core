package org.spicefactory.parsley.core.messaging;

import java.util.Set;

import javax.annotation.Nullable;

import org.spicefactory.parsley.core.messaging.receiver.MessageReceiver;

/**
 * A cache of receivers for a particular message type which MessageRouter implementations use for performance optimizations.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface MessageReceiverCache {

	/**
	 * Returns the receivers for the specified receiver kind and selector value.
	 * @param kind the kind of receivers to return
	 * @param selector the selector to use for finding matching receivers
	 * @return the receivers for the specified receiver kind and message
	 */
	Set<MessageReceiver> getReceivers(MessageReceiverKind kind, @Nullable String selector);

	/**
	 * Returns the value of the selector property of the specified message instance.
	 * @param message the message instance
	 * @return the value of the selector property of the specified message instance
	 */
	String getSelectorValue(Object message);

}
