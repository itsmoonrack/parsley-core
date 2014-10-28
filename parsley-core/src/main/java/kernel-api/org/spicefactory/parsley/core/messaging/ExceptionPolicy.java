package org.spicefactory.parsley.core.messaging;

/**
 * Enumeration for the policy to apply for un-catch exceptions.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public enum ExceptionPolicy {

	/**
	 * Constant for the policy that causes exceptions thrown by message receivers to be re-thrown.
	 */
	RETHROW,

	/**
	 * Constant for the policy that causes message processing to abort after an exception has been thrown by a message receiver.
	 */
	ABORT,

	/**
	 * Constant for the policy that causes message processing to continue after an exception has been thrown by a message receiver.
	 */
	IGNORE;

}
