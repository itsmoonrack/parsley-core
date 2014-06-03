package org.spicefactory.parsley.core.scope;

import java.util.List;

/**
 * The registry of scope instances for a single Context.
 * <p>
 * In contrast to the public API of the <code>ScopeManager</code> this registry holds <code>ScopeInfo</code> instances which represent the
 * internal API for a single scope. The registry is available for kernel services through the <code>BootstrapInfo</code> instance passed to their
 * <code>init</code> methods.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface ScopeInfoRegistry {

	/**
	 * The definitions for the scopes that were added to this Context.
	 * <p>
	 * The <code>ScopeManager</code> implementation is supposed to turn these into <code>ScopeInfo</code> instances and add them to this instance
	 * with the <code>addActiveScope</code> method.
	 * </p>
	 */
	List<ScopeDefinition> getNewScopes();

	/**
	 * The scopes that were marked for inheritance from the parent Context.
	 * <p>
	 * The <code>ScopeManager</code> implementation is supposed to pass them to the <code>addActiveScope</code> method if they should indeed get
	 * added to this Context. In most cases all inherited scopes from the parent Context will get added, but the final decision is up to the
	 * <code>ScopeManager</code>.
	 * </p>
	 */
	List<ScopeInfo> getParentScopes();

	/**
	 * The active scopes for this Context.
	 * <p>
	 * Will be empty initially, but will later hold all instances added to this registry with the <code>addActiveScope</code> method, usually
	 * during initialization of the <code>ScopeManager</code> instance.
	 * </p>
	 */
	List<ScopeInfo> getActiveScopes();

	/**
	 * Adds a new active scope for this Context, in most cases either created from one of the definitions held in the newScopes property or
	 * passed on from the existing ScopeInfo instances in the parentScopes property.
	 * @param info the scope to add for this Context.
	 */
	void addActiveScope(ScopeInfo info);

}
