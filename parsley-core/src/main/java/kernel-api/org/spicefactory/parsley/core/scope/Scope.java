package org.spicefactory.parsley.core.scope;

import java.lang.annotation.Annotation;

import org.spicefactory.parsley.core.command.CommandManager;
import org.spicefactory.parsley.core.messaging.MessageReceiverRegistry;

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
	 * Constant for the global scope.
	 * <p>
	 * A global scope spans the entire Context hierarchy and is inherited by each child Context added to the hierarchy. It is not necessarily
	 * global application-wide, since you can build disconnected Context hierarchies, although this is a rather rare use case.
	 * </p>
	 */
	final static ScopeDefinition GLOBAL_SCOPE = new GlobalScope();

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
	String getName();

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
	String getUuid();

	/**
	 * Indicates whether this scope will be inherited by child Contexts.
	 */
	boolean isInherited();

	//
	//	/**
	//	 * The root Context of this scope.
	//	 */
	//	function get rootContext () : Context;
	//
	/**
	 * The registry for receivers of application messages dispatched through this scope.
	 */
	MessageReceiverRegistry getMessageReceivers();

	/**
	 * The manager for active asynchronous commands in this scope.
	 */
	CommandManager getCommandManager();

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
	void dispatchMessage(Object message, Object selector);

	class GlobalScope implements Annotation, ScopeDefinition {

		@Override
		public String name() {
			return Scope.GLOBAL;
		}

		@Override
		public boolean inherited() {
			return true;
		}

		@Override
		public String uuid() {
			return "";
		}

		@Override
		public Class<? extends Annotation> annotationType() {
			return ScopeDefinition.class;
		}

	}

}
