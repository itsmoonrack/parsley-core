package org.spicefactory.parsley.messaging.receiver;

import java.lang.reflect.Method;

import javax.inject.Provider;

public abstract class AbstractMethodReceiver extends AbstractProviderReceiver implements MethodReceiver {

	/**
	 * Creates a new instance.
	 * @param info the mapping information for this receiver
	 */
	public AbstractMethodReceiver(MessageReceiverInfo info) {
		super(info);
	}

	@Override
	public void init(Provider<?> provider, Method method) {
		objectProvider = provider;
		targetMethod = method;
	}

	/**
	 * The method to invoke for matching messages.
	 */
	protected Method targetMethod;

	/**
	 * Returns the Class to use as the message type.
	 * <p>
	 * When the explicit type is set, this method will validate if it matches the target parameter of the method (sub-types are allowed). If
	 * omitted the message type will be solely determined by the parameter type.
	 * @param method the target method
	 * @param paramIndex the index of the parameter that expects the dispatched message
	 */
	protected void deduceMessageTypeFromParameter(Method method, int index) {
		Class<?> param = method.getParameterTypes()[index];
		if (type == Object.class) {
			type = param;
		} else if (!param.isAssignableFrom(type)) {
			throw new Error("Target " + method + ": Method parameter of type " + param.getClass() + " is not applicable to message type "
					+ type.getClass());
		}
	}
}
