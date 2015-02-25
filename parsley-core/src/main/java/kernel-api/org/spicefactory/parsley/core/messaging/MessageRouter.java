package org.spicefactory.parsley.core.messaging;

import org.spicefactory.parsley.core.command.ObservableCommand;

/**
 * Central message routing facility.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface MessageRouter {

	/**
	 * Dispatches the specified message, processing all interceptors, handlers and bindings that have registered for that message type.
	 * @param message the message to dispatch
	 * @param cache the cache of receivers for the message type
	 */
	void dispatchMessage(Message<Object> message, MessageReceiverCache cache);

	/**
	 * Processes the observers registered for the specified command and its current status.
	 * @param command the command to process the observers for
	 * @param typeCache the cache of observers matching by the type of the command
	 * @param triggerCache the cache of observers matching by the message type that triggered the command
	 */
	void observeCommand(ObservableCommand command, MessageReceiverCache typeCache, MessageReceiverCache triggerCache);

}
