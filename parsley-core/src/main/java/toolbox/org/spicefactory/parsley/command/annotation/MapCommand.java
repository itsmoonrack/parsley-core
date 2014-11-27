package org.spicefactory.parsley.command.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.spicefactory.parsley.core.messaging.Selector;
import org.spicefactory.parsley.core.scope.Scope;

/**
 * Represents a mapping in FXML or Java configuration for a managed command that gets executed when a matching message is dispatched in the
 * Context.
 * <p>
 * The Command support in Parsley allows for convenient ways to implement a command, to group them for sequential or parallel execution, or to
 * create dynamic flows with decision points based on results of individual commands. It also allows to pass results from one command to
 * subsequent commands in a sequence.
 * <p>
 * The implementation of a command follows simple naming conventions. It does not require to extends a framework base class or to implement a
 * framework interface. There are two major reasons for this design decision. The first is to keep command implementations very simple and
 * lightweight and useful even when used without the framework. The second is to keep the method signatures flexible to allow to pass data and
 * callbacks to the execute method, as you'll see in the examples. The only downside is a negligible loss in type-safety.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface MapCommand {

	/**
	 * The name of the scope in which to listen for messages.
	 */
	String scope() default Scope.GLOBAL_;

	/**
	 * The type of message (including sub-types) that should trigger command execution.
	 */
	Class<?> messageType() default Object.class;

	/**
	 * The optional selector for mapping matching messages.
	 */
	int selector() default Selector.NONE;

	/**
	 * The execution order in relation to other message receivers.
	 * <p>
	 * This order attribute affects all types of message receivers, not only those that execute commands.
	 * </p>
	 */
	int order() default Integer.MAX_VALUE;

}
