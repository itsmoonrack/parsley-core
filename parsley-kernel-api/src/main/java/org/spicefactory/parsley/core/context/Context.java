package org.spicefactory.parsley.core.context;

import javax.annotation.Nullable;

/**
 * The main interface of the IoC Container providing access to all configured objects.
 * <p>
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface Context {

	/**
	 * Adds the specified context listener to receive context events from this context. If l is null, no exception is thrown and no action is
	 * performed.
	 * @param l
	 */
	void addContextListener(ContextListener l);

	/**
	 * Removes the specified context listener so it no longer receives context events from this context. If l is null, no exception is thrown and
	 * no action is performed.
	 * @param l
	 */
	void removeContextListener(ContextListener l);

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
	 * Destroys this Context. This includes processing all life-cycle listeners for all objects that this Context has instantiated and calling
	 * their methods marked with <code>@Destroy</code>. The Context may no longer be used after calling this method.
	 */
	void destroy();

}
