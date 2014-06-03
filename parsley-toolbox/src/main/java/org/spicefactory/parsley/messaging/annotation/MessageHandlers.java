package org.spicefactory.parsley.messaging.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Message Handlers are the most common approach for the receiving side. You can declare methods to be invoked when a particular application
 * message gets dispatched. In the most simple case the method will simply be selected by parameter type:
 * <p>
 * <b> Annotation example </b>
 * <p>
 * <code>@MessageHandler private void handleLogin(LoginMessage message) {</code>
 * <p>
 * In this case the method will be invoked whenever a message of a matching type (or sub-type) gets dispatched.
 * <p>
 * There is also a variant where you split properties of the message class to arguments of the message handler method:
 * <p>
 * <code>@MessageHandler(type = LoginMessage.class, messageProperties = {"user", "roles"}) private void handleLogin(User user, Role role) {</code>
 * <p>
 * Finally you may encounter a situation where selection by message type is not sufficient. If you dispatch the same message type in different
 * scenarios and application states you may want to further refine the message selection process.
 * @see @Selector
 * @see @MessageHandler
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
@Documented
@Target(METHOD)
@Retention(RUNTIME)
public @interface MessageHandlers {
	MessageHandler[] value();
}
