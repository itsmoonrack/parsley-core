package org.spicefactory.parsley.comobserver.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.spicefactory.parsley.core.messaging.Selector;
import org.spicefactory.parsley.core.scope.Scope;

/**
 * This tag can be used for observing the status of executing commands.
 * <p>
 * <code>@CommandStatus(type = SaveUserMessage.class) public boolean isSaving;</code>
 * <p>
 * This boolean flag will always be true if one or more asynchronous commands matching the specified type and selector are currently executing.
 * It will be false otherwise. This is very convenient for tasks like disabling buttons during command execution for example.
 * <p>
 * <code>@CommandStatus(type = SaveUserMessage.class) public void isSaving(boolean value);</code>
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 * @see @CommandComplete
 * @see @CommandException
 * @see @CommandResult
 */
@Documented
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface CommandStatus {

	/**
	 * The name of the scope this tag should be applied to.
	 */
	String scope() default Scope.GLOBAL;

	/**
	 * The type of the messages the receiver wants to handle.
	 */
	Class<?> type();

	/**
	 * An optional selector value to be used in addition to selecting messages by type. Will be checked against the value of the property in the
	 * message annotated with <code>@Selector</code>.
	 */
	int selector() default Selector.NONE;

}
