package org.spicefactory.parsley.messaging.receiver;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Provider;

import org.spicefactory.parsley.core.errors.ContextError;
import org.spicefactory.parsley.core.messaging.MessageProcessor;
import org.spicefactory.parsley.core.messaging.receiver.MessageExceptionHandler;

class DefaultMessageExceptionHandler extends AbstractMethodReceiver implements MessageExceptionHandler {

	private Class<? extends Throwable> exceptionType;

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a new instance.
	 * <p>
	 * The target method must have a parameter of type <code>org.spicefactory.parsley.messaging.MessageProcessor</code> and a second parameter of
	 * type Throwable (or a sub-type).
	 */
	DefaultMessageExceptionHandler() {
		this(new MessageReceiverInfo(), null);
	}

	/**
	 * Creates a new instance.
	 * <p>
	 * The target method must have a parameter of type <code>org.spicefactory.parsley.messaging.MessageProcessor</code> and a second parameter of
	 * type Throwable (or a sub-type).
	 */
	DefaultMessageExceptionHandler(MessageReceiverInfo info) {
		this(info, null);
	}

	/**
	 * Creates a new instance.
	 * <p>
	 * The target method must have a parameter of type <code>org.spicefactory.parsley.messaging.MessageProcessor</code> and a second parameter of
	 * type Throwable (or a sub-type).
	 * @param info the mapping information for the receiver
	 * @param exceptionType the type of Throwable this handler is interested in
	 */
	DefaultMessageExceptionHandler(MessageReceiverInfo info, Class<? extends Throwable> exceptionType) {
		super(info);
		this.exceptionType = exceptionType;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	@Override
	public void init(Provider<?> provider, Method method) {
		super.init(provider, method);

		Class<?>[] params = targetMethod.getParameterTypes();
		if (params.length > 4 || params.length < 1) {
			throw new ContextError("Target " + targetMethod + ": An exception handler must have a minimum of 1 and a maximum of 4 parameters: "
					+ "(exception, [message, [selector]], [MessageProcessor])");
		}

		deduceExceptionType();
		if (deduceMessageType())
			deduceSelector();
	}

	private boolean deduceMessageType() {
		Class<?>[] params = targetMethod.getParameterTypes();
		if (params.length >= 2 && !MessageProcessor.class.isAssignableFrom(params[1])) {
			deduceMessageTypeFromParameter(targetMethod, 1);
			return true;
		}
		return false;
	}

	private void deduceSelector() {
		Class<?>[] params = targetMethod.getParameterTypes();
		if (params.length >= 3 && !MessageProcessor.class.isAssignableFrom(params[2])) {
			selector = params[2];
		}
	}

	@SuppressWarnings("unchecked")
	private void deduceExceptionType() {
		Class<?>[] params = targetMethod.getParameterTypes();
		Class<? extends Throwable> explicitType = null;
		Class<?> paramType = params[0];
		if (!Throwable.class.isAssignableFrom(paramType)) {
			throw new ContextError("Target " + targetMethod + ": First parameter must be of type Throwable or a sub-type");
		}
		if (exceptionType != Throwable.class) {
			explicitType = exceptionType;
		}
		if (explicitType == null) {
			exceptionType = (Class<? extends Throwable>) paramType;
		} else if (paramType.getClass().isAssignableFrom(explicitType)) {
			throw new ContextError("Target " + targetMethod + ": Method parameter of type " + paramType.getName()
					+ " is not applicable to exception type " + explicitType.getName());
		} else {
			exceptionType = explicitType;
		}
	}

	@Override
	public Class<? extends Throwable> getExceptionType() {
		return exceptionType;
	}

	@Override
	public void handleException(MessageProcessor<?> processor, Throwable exception) {
		List<Object> params = new ArrayList<Object>();
		params.add(exception);

		int count = targetMethod.getParameterTypes().length;

		if (count >= 2) {
			Class<?> param1 = targetMethod.getParameterTypes()[1];
			if (param1.isAssignableFrom(MessageProcessor.class)) {
				params.add(processor);
			} else {
				params.add(processor.getMessage().getInstance());
			}
		}
		if (count >= 3) {
			Class<?> param2 = targetMethod.getParameterTypes()[2];
			if (param2.isAssignableFrom(MessageProcessor.class)) {
				params.add(processor);
			} else if (processor.getMessage().getSelector() instanceof Class) {
				params.add(null);
			}
		}
		if (count >= 4) {
			params.add(processor);
		}

		try {
			targetMethod.setAccessible(true);
			targetMethod.invoke(objectProvider.get(), params.toArray());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////
}
