package org.spicefactory.parsley.messaging.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.spicefactory.parsley.core.messaging.Selector;
import org.spicefactory.parsley.core.scope.Scope;

/**
 * Message Handlers are the most common approach for the receiving side. You can declare methods to be invoked on @Singleton instances when a
 * particular application message gets dispatched. In the most simple case the method will simply be selected by parameter type:
 * <p>
 * <b> Annotation example </b>
 * <p>
 *
 * <pre>
 * <code>@MessageHandler
 * protected void handleLogin(LoginMessage message) {
 * </code>
 * </pre>
 * <p>
 * In this case the method will be invoked whenever a message of a matching type (or sub-type) gets dispatched. The recommended visibility for
 * such methods is protected so it remain testable by sub-classes and its not exposed as public so clients of the receiving class can not
 * triggers the method by direct call.
 * <p>
 * There is also a variant where you split properties of the message class to arguments of the message handler method:
 * <p>
 *
 * <pre>
 * <code>@MessageHandler(type = LoginMessage.class, messageProperties = {"user", "roles"})
 * public void handleLogin(User user, Role role) {
 * </code>
 * </pre>
 * <p>
 * This is useful for public API methods implementing an interface which does not depends on your domain messages but instead plain objects as it
 * would be recommended in most software engineering books.
 * <p>
 * Finally you may encounter a situation where selection by message type is not sufficient. If you dispatch the same message type in different
 * scenarios and application states you may want to further refine the message selection process.
 * @see @Selector for details.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface MessageHandler {

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

	/**
	 * Optional list of names of properties of the message that should be used as method parameters instead passing the message itself as a
	 * parameter.
	 */
	String[] messagesProperties() default {};
}
