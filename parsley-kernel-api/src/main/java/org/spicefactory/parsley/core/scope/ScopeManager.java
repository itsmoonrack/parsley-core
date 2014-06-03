package org.spicefactory.parsley.core.scope;

import java.util.List;

import javax.annotation.Nullable;

import org.spicefactory.parsley.core.command.ObservableCommand;

/**
 * Responsible for managing the scopes associated with a single Context. Each Context has a unique set of scopes associated with it. It may be
 * any combination of scopes inherited from parent Contexts or explicitly created for a Context. When no custom scopes are created the default
 * setup is that each Context is associated with two scopes: One global scope, shared throughout the entire Context hierarchy, and a local scope
 * that just spans a single Context.
 * <p>
 * Scopes allow to create segments in your application where application messages or life-cycle listeners are only effective for a particular
 * scope, which in turn may span multiple Contexts.
 * </p>
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface ScopeManager {

	/**
	 * Indicates whether this manager contains a scope with the specified name.
	 * @param name the name of the scope to look for
	 * @return true if this manager contains a scope with the specified name
	 */
	boolean hasScope(String name);

	/**
	 * Returns the scope instance for the specified scope name. If the scope name is omitted, the default receiver scope is returned.
	 * @param name the name of the scope to look for
	 * @return the Scope instance for the specified name
	 */
	Scope getScope(@Nullable String name);

	/**
	 * Returns all scope instances managed by this instance.
	 * @return all scope instances managed by this instance
	 */
	List<Scope> getAllScopes();

	/**
	 * Dispatches a message through all scopes managed by this instance. In many cases you'll want to dispatch application messages through all
	 * scopes so that the receiving side can decide which scope it wants to listen for.
	 * @param message the message to dispatch
	 * @param selector the selector to use if it cannot be determined from the message instance itself
	 */
	void dispatchMessage(Object message, @Nullable Object selector);

	/**
	 * Observes the specified command and dispatches messages to registered observers of all scopes managed by this instance when the state of
	 * the command changes.
	 * @param command the command to observe
	 */
	void observeCommand(ObservableCommand command);

}
