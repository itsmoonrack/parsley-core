package org.spicefactory.parsley.messaging.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.spicefactory.parsley.core.scope.Scope;

/**
 * Represents an annotation that can be used on properties where a message dispatcher function should be injected.
 * <p>
 * This is an alternative to declaring managed events, useful when working with message types which do not extend <tt>java.util.EventObject</tt>.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
@Documented
@Target(FIELD)
@Retention(RUNTIME)
public @interface MessageDispatcher {

	/**
	 * The scope messages should be dispatched through.
	 * <p>
	 * If this attribute is omitted the message will be dispatched through all scopes of the corresponding Context.
	 */
	String scope() default Scope.GLOBAL;
}
