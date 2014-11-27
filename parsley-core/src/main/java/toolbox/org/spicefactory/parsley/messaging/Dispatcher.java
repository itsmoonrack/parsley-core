package org.spicefactory.parsley.messaging;

/**
 * FunctionDispatcher offers the option to use any class as an application message, whether it extends <tt>java.util.EventObject</tt> or not.
 * <p>
 * You can request the framework to inject a message dispatcher function that you can use for custom application messages. Assuming you created
 * the following simple message class:
 *
 * <pre>
 * <code>
 * class LoginMessage {
 *     public User user;
 *     public String role;
 *
 *     public LoginMessage(User user, String role) {
 *         this.user = user;
 *         this.role = role;
 *     }
 * }
 * </code>
 * </pre>
 *
 * You can then use it in a service like this:
 *
 * <pre>
 * <code>
 * class LoginServiceImpl implements LoginService {
 *     @MessageDispatcher
 *     FunctionDispatcher dispatcher;
 *     [...]
 *     public void login(User user) {
 *         [...]
 *         dispatcher.dispatchMessage(new LoginMessage(user, user.role));
 *     }
 * }
 * </code>
 * </pre>
 *
 * Your service does not extend <tt>EventDispatcher</tt>. Instead it declares a field of type <tt>FunctionDispatcher</tt> annotated with the
 * <tt>@MessageDispatcher</tt> annotation which instructs Parsley to inject a message dispatcher function on object creation. You can then simply
 * pass any kind of object to this dispatcher function.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface Dispatcher {

	/**
	 * Dispatches the specified message through the ScopeManager associated with this reference.
	 * @param message the message to dispatch
	 * @param selector the selector to use if it cannot be determined from the message instance itself
	 */
	// Delegated to dispatchMessage method on concrete MessageDispatcher instance.
	void dispatchMessage(Object message, int selector);

	/**
	 * Dispatches the specified message through the ScopeManager associated with this reference.
	 * @param message the message to dispatch
	 */
	void dispatchMessage(Object message);

}
