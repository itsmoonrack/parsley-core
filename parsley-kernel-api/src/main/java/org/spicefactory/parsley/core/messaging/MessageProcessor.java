package org.spicefactory.parsley.core.messaging;

import javax.annotation.Nullable;

/**
 * Responsible for processing a single message. Will be passed to registered message interceptors and error handlers which may chose to cancel or
 * suspend and later resume the message processing.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface MessageProcessor {

	/**
	 * Returns the message instance.
	 */
	Message message();

	/**
	 * Returns the current state of this processor.
	 */
	MessageState state();

	/**
	 * Cancels processing of this message. No further handlers will be invoked and all resources associated with this message are disposed.
	 */
	void cancel();

	/**
	 * Suspends processing of the message.
	 * <p>
	 * No further handlers will be invoked before <code>resume</code> gets called on this processor. To permanently discard this message call
	 * <code>cancel</code> to free all resources associated with this message.
	 */
	void suspend();

	/**
	 * Resumes with message processing, invoking the next receiver. May only be called after <code>suspend</code> has been called on this
	 * processor
	 */
	void resume();

	/**
	 * Rewinds the processor so it will start with the first interceptor or handler again the next time the proceed method gets invoked.
	 * <p>
	 * Calling this method also causes all receivers to be refetched from the registry and thus takes into account any new receivers registered
	 * after processing this message started.
	 */
	void rewind();

	/**
	 * Sends the response to the Context the message originated from.
	 * <p>
	 * It does not send this message to the sending object instance only, as this would not be very helpful in most cases. Essentially this
	 * method is just a short cut for calling: <code><pre>processor.senderContext.scopeManager.dispatchMessage(new MyMessage());</pre></code>
	 * Note that the response is dispatched through all scopes of the Context of the sending instance, including the global scope. Therefore for
	 * point-to-point messaging the receiver of the response should listen to the local scope (or a custom scope) instead.
	 * @param message the message to dispatch
	 * @param selector the selector to use if it cannot be determined from the message instance itself
	 */
	void sendResponse(Object message, @Nullable Object selector);

}
