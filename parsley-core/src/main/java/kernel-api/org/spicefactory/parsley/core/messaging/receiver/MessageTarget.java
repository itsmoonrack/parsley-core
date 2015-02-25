package org.spicefactory.parsley.core.messaging.receiver;

import org.spicefactory.parsley.core.messaging.MessageProcessor;

/**
 * Represent a regular target for a message. This interface should be implemented by all general-purpose message receivers which are not
 * interceptors or error handlers. Built-in implementations are MessageHandler, MessagePropertyHandler, MessageBinding and CommandProxy.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface MessageTarget extends MessageReceiver {

	/**
	 * Handles a message for this target.
	 * <p>
	 * The specified processor may be used to control the message processing, like canceling or suspending a message.
	 * @param processor the processor for the message
	 */
	void handleMessage(MessageProcessor<Object> processor);

}
