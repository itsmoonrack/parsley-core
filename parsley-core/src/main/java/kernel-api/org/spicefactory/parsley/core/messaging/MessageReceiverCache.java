package org.spicefactory.parsley.core.messaging;

import java.util.List;

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
	List<MessageReceiver> getReceivers(MessageReceiverKind kind, int selector);

	/**
	 * Returns the value of the selector property of the specified message instance.
	 * @param message the message instance
	 * @return the value of the selector property of the specified message instance
	 */
	int getSelectorValue(Object message);

}
