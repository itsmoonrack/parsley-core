package org.spicefactory.parsley.core.messaging;

/**
 * Enumeration for the current state of a processed message.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public enum MessageState {

	/**
	 * Constant for the active state. While in this state a message processor will continue to invoke all remaining receivers.
	 */
	ACTIVE,

	/**
	 * Constant for the cancelled state. When in this state no further processing of the message is possible.
	 */
	CANCELLED,

	/**
	 * Constant for the suspended state. When in this state processing of message receivers is suspended, but may be resumed later by calling
	 * <code>MessageProcessor.resume()</code>
	 */
	SUSPENDED,

	/**
	 * Constant for the complete state. This state signals that processing of the message completed successfully.
	 */
	COMPLETE;

}
