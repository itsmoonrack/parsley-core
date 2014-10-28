package org.spicefactory.parsley.core.registry;

import org.spicefactory.parsley.core.context.Context;

/**
 * A registry of abstract objects.
 * <p>
 * Parsley does not come with implementation of object registry so you must use a suitable IoC framework such as Guice or Spring at runtime.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public interface Registry {

	/**
	 * The Context associated with this registry.
	 * <p>
	 * During processing of objects the Context is not fully configured, and in particular any attempt to fetch objects from
	 * <code>getInstance()</code> will lead to an Error. It is primarily exposed in case an extension wants to keep a reference to the Context
	 * for later use.
	 */
	Context getContext();

	/**
	 * Returns the appropriate instance for the given injection type; When feasible, avoid using this method, in favor of having Context inject
	 * your dependencies ahead of time.
	 */
	<T> T getInstance(Class<T> type);

}
