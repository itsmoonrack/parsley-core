package org.spicefactory.parsley.core.scope;

import org.spicefactory.parsley.core.command.CommandManager;
import org.spicefactory.parsley.core.command.ObservableCommand;
import org.spicefactory.parsley.core.context.Context;
import org.spicefactory.parsley.core.messaging.MessageReceiverCache;
import org.spicefactory.parsley.core.messaging.MessageReceiverRegistry;

/**
 * Holds the definition and state (like all registered message receivers) for a single scope. Instances of this class will be shared by all
 * ScopeManagers of all Context instances that a scope is associated with.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface ScopeInfo {

	/**
	 * The name of the scope.
	 */
	String getName();

	/**
	 * The unique id of the scope.
	 */
	String getUuid();

	/**
	 * Indicates whether this scope will be inherited by child Contexts.
	 */
	boolean isInherited();

	/**
	 * The root Context of this scope.
	 */
	Context getRootContext();

	/**
	 * The registry for receivers of application messages dispatched through this scope.
	 */
	MessageReceiverRegistry getMessageReceivers();

	/**
	 * Returns the cache of message receivers for the specified message type.
	 * <p>
	 * If no cache for that type exists yet, implementations should create and return a new cache instance.
	 * @param type the message type to return the receiver cache for
	 * @return the cache of message receivers for the specified message type
	 */
	MessageReceiverCache getMessageReceiverCache(Class<?> type);

	/**
	 * The manager for active commands in this scope.
	 */
	CommandManager getCommandManager();

	/**
	 * Adds an active command to the command manager of this scope.
	 * <p>
	 * As the CommandManager is a public API it does not contain a comparable method itself.
	 */
	void addActiveCommand(ObservableCommand command);

}
