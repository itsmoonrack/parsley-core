package org.spicefactory.parsley.core.context;

import javax.annotation.Nullable;

import org.spicefactory.lib.event.EventDispatcher;
import org.spicefactory.parsley.core.scope.ScopeManager;
import org.spicefactory.parsley.core.view.ViewManager;

/**
 * The main interface of the IoC Container providing access to all configured objects.
 * <p>
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface Context extends EventDispatcher<ContextListener> {

	/**
	 * Injects dependencies into the fields and methods of {@code instance}. Ignores the presence or absence of an injectable constructor.
	 * <p>
	 * Whenever Context creates an instance, it performs this injection automatically (after first performing constructor injection), so if
	 * you're able to let Context create all your objects for you, you'll never need to use this method.
	 * @param instance to inject members on
	 */
	void injectMembers(Object instance);

	/**
	 * Returns the appropriate instance for the given injection type; When feasible, avoid using this method, in favor of having Context inject
	 * your dependencies ahead of time.
	 */
	<T> T getInstance(Class<T> type);

	/**
	 * Gets parents Context. Null if top-level parent.
	 */
	@Nullable
	Context[] getParents();

	/**
	 * Gets scope manager that handles all scopes that this Context belongs to.
	 * <p>
	 * This includes scopes inherited from parent Contexts as well as scopes created for this Context.
	 */
	ScopeManager getScopeManager();

	/**
	 * Gets view manager used to dynamically wire view instances to this Context.
	 */
	ViewManager getViewManager();

	/**
	 * Destroys this Context. This includes processing all life-cycle listeners for all objects that this Context has instantiated and calling
	 * their methods marked with <code>@Destroy</code>. The Context may no longer be used after calling this method.
	 */
	void destroy();

}
