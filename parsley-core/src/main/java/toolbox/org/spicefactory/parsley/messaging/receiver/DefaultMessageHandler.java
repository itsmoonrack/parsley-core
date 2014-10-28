package org.spicefactory.parsley.messaging.receiver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Provider;

import org.spicefactory.parsley.core.messaging.MessageProcessor;
import org.spicefactory.parsley.core.messaging.receiver.MessageTarget;
import org.spicefactory.parsley.messaging.annotation.MessageHandler;

class DefaultMessageHandler extends AbstractMethodReceiver implements MessageTarget {

	private boolean isInterceptor;
	private Field[] messageFields;
	private final String[] messageProperties;

	/**
	 * Creates a new instance.
	 * @param info the mapping information for the receiver
	 */
	DefaultMessageHandler(MessageHandler info) {
		super(info);
		this.messageProperties = info.messagesProperties();

	}

	@Override
	public void init(Provider<?> provider, Method method) {
		super.init(provider, method);

		if (messageProperties.length == 0) {
			deduceMessageType();
			//			deduceSelector();
		} else {
			setMessageProperties();
		}

		final Class<?>[] params = targetMethod.getParameterTypes();
		isInterceptor = (params.length > 0) && params[params.length - 1].equals(MessageProcessor.class);
	}

	private void deduceMessageType() {
		final int params = targetMethod.getParameterTypes().length;
		if (params > 3) {
			throw new Error("Target " + targetMethod + ": At most 3 parameters allowed for this type of message handler.");
		}
		if (params > 1 || (params == 1 && !targetMethod.getParameterTypes()[0].equals(MessageProcessor.class))) {
			deduceMessageTypeFromParameter(targetMethod, 0);
		}
	}

	//	private void deduceSelector() {
	//		final Class<?>[] params = targetMethod.getParameterTypes();
	//		if (params.length >= 2) {
	//			final Class<?> param = params[1];
	//			if (!param.isAssignableFrom(MessageProcessor.class)) {
	//				selector = param;
	//			}
	//		}
	//	}

	private void setMessageProperties() {
		final Class<?>[] params = targetMethod.getParameterTypes();
		final Annotation[][] annots = targetMethod.getParameterAnnotations();
		int requiredParams = 0;

		for (Annotation[] annotations : targetMethod.getParameterAnnotations()) {
			if (requiredParam(annotations)) {
				requiredParams++;
			}
		}

		// Special handling for MessageProcessor.
		if (requiredParams > 0) {
			final Class<?> last = params[params.length - 1];
			if (last.isAssignableFrom(MessageProcessor.class) && requiredParam(annots[params.length - 1])) {
				requiredParams--;
			}
		}

		if (requiredParams > messageProperties.length) {
			throw new Error("Number of specified parameter names does not match the number of required parameters of " + targetMethod
					+ ". Required: " + requiredParams + " - actual: " + messageProperties.length);
		}

		final List<Field> resolved = new ArrayList<Field>(requiredParams);
		for (String name : messageProperties) {
			try {
				resolved.add(type.getDeclaredField(name));
			}
			catch (NoSuchFieldException e) {
				throw new Error("Message type " + type + " does not contain a field with name " + name + ".", e);
			}
		}

		messageFields = resolved.toArray(new Field[resolved.size()]);
	}

	@Override
	public int order() {
		if (super.order() != Integer.MAX_VALUE) {
			return super.order();
		}
		return (isInterceptor) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
	}

	/**
	 * Returns either or not a parameter (given the annotation list) is required.
	 * @param annotations
	 * @return true if parameter is @Nonnull or is not @Nullable, false otherwise
	 */
	private boolean requiredParam(Annotation[] annotations) {
		for (Annotation annotation : annotations) {
			if (annotation instanceof Nullable) {
				return false;
			} else if (annotation instanceof Nonnull) {
				return true;
			}
		}
		return true;
	}

	@Override
	public void handleMessage(MessageProcessor processor) {
		try {
			final List<Object> args = new ArrayList<Object>();
			if (messageFields == null) {
				final int count = targetMethod.getParameterTypes().length;
				if (count >= 1) {
					if (MessageProcessor.class.isAssignableFrom(targetMethod.getParameterTypes()[0])) {
						args.add(processor);
					} else {
						args.add(processor.message().instance());
					}
				}
				if (count >= 2) {
					if (targetMethod.getParameterTypes()[1].equals(MessageProcessor.class)) {
						args.add(processor);
					} else {
						args.add(processor.message().selector());
						if (count == 3) {
							args.add(processor);
						}
					}
				}
			} else {
				for (Field field : messageFields) {
					field.setAccessible(true);
					args.add(field.get(processor.message().instance()));
				}
				if (targetMethod.getParameterTypes().length > args.size()) {
					args.add(processor);
				}
			}

			targetMethod.setAccessible(true);
			targetMethod.invoke(objectProvider.get(), args.toArray());
		}
		catch (Exception e) {
			throw new Error("Message type " + type + " cannot be processed.", e);
		}
	}
}
