package org.spicefactory.parsley.core.scope;

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
	String name();

	/**
	 * The registry for receivers of application messages dispatched through this scope.
	 */
	MessageReceiverRegistry messageReceivers();

	/**
	 * Returns the cache of message receivers for the specified message type.
	 * <p>
	 * If no cache for that type exists yet, implementations should create and return a new cache instance.
	 * @param type the message type to return the receiver cache for
	 * @return the cache of message receivers for the specified message type
	 */
	MessageReceiverCache getMessageReceiverCache(Class<?> type);

}
