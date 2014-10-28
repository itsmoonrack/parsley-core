package org.spicefactory.parsley.comobserver.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.spicefactory.parsley.core.messaging.Selector;
import org.spicefactory.parsley.core.scope.Scope;

/**
 * This tag can be used for receiving the eventual fault events or the other exceptions.
 * <p>
 * <code>@CommandException protected void handleResult(Exception e, SaveUserMessage trigger);</code>
 * <p>
 * The parameters are again both optional and the rules for matching are the same as for @CommandResult.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 * @see @CommandResult
 * @see @CommandComplete
 * @see @CommandStatus
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface CommandException {

	/**
	 * The name of the scope this tag should be applied to.
	 */
	String scope() default Scope.GLOBAL_;

	/**
	 * The type of the messages the receiver wants to handle.
	 */
	Class<?> type() default Object.class;

	/**
	 * An optional selector value to be used in addition to selecting messages by type. Will be checked against the value of the property in the
	 * message annotated with <code>@Selector</code>.
	 */
	String selector() default Selector.NONE;

	/**
	 * The execution order for this receiver. Will be processed in ascending order.
	 * <p>
	 * The default is <code>int.MAX_VALUE</code>.
	 * </p>
	 */
	int order() default Integer.MAX_VALUE;

}
