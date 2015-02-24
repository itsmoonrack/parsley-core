package org.spicefactory.parsley.messaging.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.spicefactory.parsley.core.scope.Scope;

/**
 * Represents an annotation, FXML or XML tag that can be used on classes that dispatch events that should be dispatched through Parsleys central
 * message router.
 * <p>
 * May only be place on classes that implement <code>IEventDispatcher</code>. The class definition should contain additional regular
 * <code>@Event</code> annotations for all events it dispatches.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface ManagedEvents {

	/**
	 * The event ids/types of all events dispatched by the annotated class that should be managed by Parsley.
	 */
	int[] value() default {};

	/**
	 * The scope these managed events should be dispatched to.
	 * <p>
	 * If this attribute is omitted the event will be dispatched through all scopes of the corresponding Context.
	 */
	String scope() default Scope.GLOBAL;

}
