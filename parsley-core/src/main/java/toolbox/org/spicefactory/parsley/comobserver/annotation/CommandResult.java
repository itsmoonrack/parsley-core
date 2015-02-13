package org.spicefactory.parsley.comobserver.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.spicefactory.parsley.core.messaging.Selector;
import org.spicefactory.parsley.core.scope.Scope;

/**
 * This annotation can be used to obtains the result produced by a commend in a different object. (It is not needed and should not be used to
 * define a result handler inside the command itself).
 *
 * <pre>
 * <code>
 * @CommandResult
 * public void handleResult(User user, LoginMessage trigger);</code>
 * </pre>
 *
 * In this case the User instance returned by the remote call will be passed to the result handler alongside the original message that triggered
 * the action. Like with normal message handlers the parameter type for the message is used to determine which handlers to invoke. It is always a
 * combination of message type (polymorphically) and an optional selector value which serves as a secondary selection key. The type of the result
 * also has to match for this method to get invoked.
 * <p>
 * If the command that produced the User instance is part of a sequence that was triggered by the specified message type, then by default the
 * result handler will only get invoked after the entire sequence has been completed. This way you do not need to bother processing the result in
 * case a subsequent command causes the sequence to abort with an error for example.
 * <p>
 * In case you do need to process the result as early as possible, you can use the immediate attribute:
 * <p>
 *
 * <pre>
 * <code>
 * @CommandResult(immediate = true)
 * public void handleResult(User user, LoginMessage trigger);</code>
 * </pre>
 * <p>
 * This attribute does not have any effect if the command is not part of a sequence or flow.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 * @see @CommandComplete
 * @see @CommandException
 * @see @CommandStatus
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface CommandResult {

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
	 * Indicates whether the result should be processed immediately after the command finished executing (when set to true) or after the entire
	 * sequence or flow finished (when set to false - the default). Has no effect on the execution of a single command that is not part of a
	 * sequence or flow.
	 */
	boolean immediate() default false;
}
