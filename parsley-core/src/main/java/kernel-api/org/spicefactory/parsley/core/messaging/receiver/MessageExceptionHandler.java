package org.spicefactory.parsley.core.messaging.receiver;

import org.spicefactory.parsley.core.messaging.MessageProcessor;

/**
 * Handles exceptions thrown by regular message targets or interceptors.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface MessageExceptionHandler extends MessageReceiver {

	/**
	 * The type of Exception this handler is interested in.
	 * <p>
	 * Like with matching message classes this works in a polymorphic way. Specifying the base Exception class creates an ExceptionHandler that
	 * handles all exceptions for a particular message type.
	 * </p>
	 */
	Class<? extends Throwable> getExceptionType();

	/**
	 * Handles an exception thrown by a regular message target or interceptor.
	 * <p>
	 * Processing further exception handlers and targets will only happen if proceed is called on the specified processor.
	 * @param processor the processor for the message
	 * @param exception the exception thrown by a message target
	 */
	void handleException(MessageProcessor<?> processor, Throwable exception);

}
