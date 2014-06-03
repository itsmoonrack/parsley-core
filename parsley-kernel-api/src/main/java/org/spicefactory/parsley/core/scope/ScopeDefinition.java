package org.spicefactory.parsley.core.scope;

/**
 * The definition for a single scope.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public @interface ScopeDefinition {

	/**
	 * The name of the scope.
	 */
	String name();

	/**
	 * Indicates whether this scope will be inherited by child Contexts.
	 */
	boolean inherited();

	/**
	 * The unique id of this scope. See {@link Scope#uuid()} method for details.
	 */
	String uuid() default "";

}
