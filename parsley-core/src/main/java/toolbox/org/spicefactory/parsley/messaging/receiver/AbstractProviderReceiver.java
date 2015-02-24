package org.spicefactory.parsley.messaging.receiver;

import javax.inject.Provider;

/**
 * Abstract base class for all types of message receivers that use an Object Provider for determining the target instance handling the message.
 * <p>
 * An object provider is a convenient way to register lazy initializing message receivers where the instantiation of the actual instance handling
 * the message may be deferred until the first matching message is dispatched.
 * </p>
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public abstract class AbstractProviderReceiver extends AbstractMessageReceiver {

	/**
	 * Provider for target instances for this receiver.
	 */
	protected Provider<?> objectProvider;

	/**
	 * Creates a new instance.
	 * @param info the mapping information for this receiver
	 */
	public AbstractProviderReceiver(MessageReceiverInfo info) {
		super(info);
	}
}
