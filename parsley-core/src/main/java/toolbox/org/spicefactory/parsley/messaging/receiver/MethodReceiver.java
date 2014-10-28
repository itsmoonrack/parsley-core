package org.spicefactory.parsley.messaging.receiver;

import java.lang.reflect.Method;

import javax.inject.Provider;

import org.spicefactory.parsley.core.messaging.receiver.MessageReceiver;

/**
 * Represents a single method that acts as a message receiver.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public interface MethodReceiver extends MessageReceiver {

	/**
	 * Initializes this receiver, passing the target object provider and method.
	 * @param provider the provider to use for obtaining target instances for matching messages
	 * @param method the target method on instances obtained by the provider that handles the message
	 */
	void init(Provider<?> provider, Method method);

}
