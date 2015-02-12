package org.spicefactory.parsley.core.messaging;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.spicefactory.parsley.core.messaging.receiver.MessageExceptionHandler;
import org.spicefactory.parsley.core.scope.Scope;

/**
 * Settings for MessageRouter instances.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface MessageSettings {

	/**
	 * The policy to apply for unhandled exceptions.
	 * <p>
	 * An unhandled exception is an exception thrown by a message handler where no matching exception handler was registered for.
	 */
	ExceptionPolicy unhandledException() default ExceptionPolicy.IGNORE;

	/**
	 * Exception handlers that were registered for these settings.
	 */
	Class<? extends MessageExceptionHandler>[] exceptionHandlers() default {};

	/**
	 * The default scope to use for message receivers and observers.
	 * <p>
	 * Default to Scope.GLOBAL is not specified.
	 * <p>
	 * In a modular application it is not uncommon that most message receivers are only interested in local messages. Switching the default
	 * allows to avoid specifying the local scope explicitly on all meta-data tags.
	 */
	String defaultReceiverScope() default Scope.GLOBAL;

}
