package org.spicefactory.parsley.messaging.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.spicefactory.parsley.core.messaging.Selector;
import org.spicefactory.parsley.core.scope.Scope;

/**
 * Parsley allows to configure a method to be invoked whenever a handler for a matching message threw an Exception:
 * <p>
 * <b> Annotation example </b>
 * <p>
 * <code>@MessageException void handleException(IOException e, LoginMessage message);</code>
 * <p>
 * In the example above the exception handler would be invoked whenever any handler for a message of type LoginMessage throws an IOException. It
 * is possible to chose the matching message type and the matching Exception type and of course, like with all annotations, specify an additional
 * selector attribute.
 * <p>
 * It is also possible to create a global handler, for every type of exception and any type of messages:
 * <p>
 * <code>@MessageException void handleException(Exception e);</code>
 * <p>
 * As with all message handlers an exception handler can also accept an argument of type MessageProcessor in case you want to cancel or suspend
 * processing, or send a message to the sender Context:
 * <p>
 * <code>@MessageException void handleException(Exception e, MessageProcessor processor);</code>
 * <p>
 * Only the first parameter for the exception is required. The ones for the message and the processor are both optional.
 * <p>
 * Finally, since an exception handler configured with the annotation shown above always listens to a single scope, you may want to add an
 * exception handler that will be automatically attached to every scope created for an application. You can do that programmatically through the
 * BootstrapDefaults:
 * <p>
 * <code>DefaultMessageExceptionHandler handler = new DefaultMessageExceptionHandler();</code> TODO: doc to complete.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface MessageException {

	/**
	 * The name of the scope this tag should be applied to.
	 */
	String scope() default Scope.GLOBAL;

	/**
	 * The type of the messages the receiver wants to handle.
	 */
	Class<?> type() default Object.class;

	/**
	 * An optional selector value to be used in addition to selecting messages by type. Will be checked against the value of the property in the
	 * message annotated with <code>@Selector</code>.
	 */
	int selector() default Selector.NONE;

	/**
	 * The execution order for this receiver. Will be processed in ascending order.
	 * <p>
	 * The default is <code>int.MAX_VALUE</code>.
	 * </p>
	 */
	int order() default Integer.MAX_VALUE;

	/**
	 * The type of the exception that this handler is interested in.
	 * <p>
	 * The default is the top level Throwable class.
	 */
	Class<Throwable> exceptionType() default Throwable.class;

}
