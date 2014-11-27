package org.spicefactory.parsley.comobserver.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.spicefactory.parsley.core.messaging.Selector;
import org.spicefactory.parsley.core.scope.Scope;

/**
 * This tag can be used when you are not interested in the actual result, but instead only want to execute some logic triggered by the completion
 * of the command.
 * <p>
 * <code>@CommandComplete protected void userSaved(SaveUserMessage message);</code>
 * <p>
 * This means that this method would get invoked whenever any command triggered by a SaveUserMessage has completed. In case of sequences this
 * means the method gets invoked after all commands in that sequence completed successfully.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 * @see @CommandException
 * @see @CommandResult
 * @see @CommandStatus
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface CommandComplete {

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
	int selector() default Selector.NONE;

	/**
	 * The execution order for this receiver. Will be processed in ascending order.
	 * <p>
	 * The default is <code>int.MAX_VALUE</code>.
	 * </p>
	 */
	int order() default Integer.MAX_VALUE;

}
