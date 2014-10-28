package org.spicefactory.parsley.core.messaging;

import org.spicefactory.parsley.core.messaging.receiver.CommandObserver;
import org.spicefactory.parsley.core.messaging.receiver.MessageExceptionHandler;
import org.spicefactory.parsley.core.messaging.receiver.MessageTarget;

/**
 * Registry for receivers of messages dispatched through a MessageRouter.
 * <p>
 * There are three types of receivers: First the regular targets like MessageHandlers and MessageBindings, second interceptors which have some
 * additional power and may cancel or suspend the processing of a message and finally error handlers which are only invoked in case a regular
 * target or an interceptor threw an error.
 * </p>
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface MessageReceiverRegistry {

	/**
	 * Adds a regular target (like a MessageHandler or MessageBinding) to this registry.
	 * @param target the target to add to this registry
	 */
	void addTarget(MessageTarget target);

	/**
	 * Removes a regular target (like a MessageHandler or MessageBinding) from this registry.
	 * @param target the target to remove from this registry
	 */
	void removeTarget(MessageTarget target);

	/**
	 * Adds an error handler to this registry.
	 * @param handler the error handler to add to this registry
	 */
	void addExceptionHandler(MessageExceptionHandler handler);

	/**
	 * Removes an error handler from this registry.
	 * @param handler the error handler to remove from this registry
	 */
	void removeErrorHandler(MessageExceptionHandler handler);

	/**
	 * Adds an observer for a matching command execution to this registry.
	 * @param observer the observer to add to this registry
	 */
	void addCommandObserver(CommandObserver observer);

	/**
	 * Removes an observer for a matching command execution from this registry.
	 * @param observer the observer to remove from this registry
	 */
	void removeCommandObserver(CommandObserver observer);

}
