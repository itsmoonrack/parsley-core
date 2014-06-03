package org.spicefactory.parsley.core.scope;

import javax.annotation.Nullable;

/**
 * Represents a single scope.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface Scope {

	/**
	 * Constant for the name of the global scope.
	 * <p>
	 * A global scope spans the entire Context hierarchy and is inherited by each child Context added to the hierarchy. It is not necessarily
	 * global application-wide, since you can build disconnected Context hierarchies, although this is a rather rare use case.
	 * </p>
	 */
	final static String GLOBAL = "global";

	/**
	 * Constant for the name of the local scope.
	 * <p>
	 * A local scope only spans a single Context. Such a scope is automatically created for each Context created in an application.
	 * </p>
	 */
	final static String LOCAL = "local";

	/**
	 * The name of the scope.
	 */
	String name();

	/**
	 * The unique id of this scope.
	 * <p>
	 * In small or mid-size projects an id is often not needed, the framework will auto-generate the id in these cases and it can simply be
	 * ignored. In a big modular application there may be the need to address one particular scope within the system and the scheme of ids
	 * assigned to the scopes is often application-specific.
	 * <p>
	 * </p>
	 * The uid may be used to identify persistent published values or to explicitly route messages. </p>
	 */
	String uuid();

	/**
	 * Indicates whether this scope will be inherited by child Contexts.
	 */
	boolean inherited();

	//
	//	/**
	//	 * The root Context of this scope.
	//	 */
	//	function get rootContext () : Context;
	//
	//	/**
	//	 * The registry for receivers of application messages dispatched through this scope.
	//	 */
	//	function get messageReceivers () : MessageReceiverRegistry;
	//
	//	/**
	//	 * The manager for active asynchronous commands in this scope.
	//	 */
	//	function get commandManager () : CommandManager;
	//
	//	/**
	//	 * The manager for publishers and subscribers of the decoupled binding facility.
	//	 */
	//	function get bindingManager () : BindingManager;
	//
	//	/**
	//	 * The manager for values persisted by publishers.
	//	 */
	//	function get persistenceManager () : PersistenceManager;
	//
	//	/**
	//	 * The registry for observers of lifecycle events dispatched by objects within this scope.
	//	 */
	//	function get lifecycleObservers () : LifecycleObserverRegistry; 
	//
	//	/**
	//	 * Custom extensions registered for this scope.
	//	 */
	//	function get extensions () : ScopeExtensions;

	/**
	 * Dispatches a message through this scope.
	 * @param message the message to dispatch
	 * @param selector the selector to use if it cannot be determined from the message instance itself
	 */
	void dispatchMessage(Object message, @Nullable Object selector);

}
