package org.spicefactory.parsley.core.scope;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The definition for a single scope.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface ScopeDefinition {

	/**
	 * The name of the scope.
	 */
	String name();

	/**
	 * Indicates whether this scope will be inherited by child Contexts.
	 */
	boolean inherited() default true;

	/**
	 * The unique id of this scope. See {@link Scope#uuid()} method for details.
	 */
	String uuid() default "";

}
