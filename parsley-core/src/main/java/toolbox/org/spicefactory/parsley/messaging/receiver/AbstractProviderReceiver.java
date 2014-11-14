package org.spicefactory.parsley.messaging.receiver;

import javax.inject.Provider;

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
