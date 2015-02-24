package org.spicefactory.parsley.comobserver.receiver;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Provider;

import org.spicefactory.lib.command.adapter.CommandAdapter;
import org.spicefactory.parsley.core.command.CommandObserverProcessor;
import org.spicefactory.parsley.core.errors.ContextError;
import org.spicefactory.parsley.core.messaging.MessageProcessor;
import org.spicefactory.parsley.core.messaging.MessageReceiverKind;
import org.spicefactory.parsley.core.messaging.receiver.CommandObserver;
import org.spicefactory.parsley.messaging.receiver.AbstractMethodReceiver;
import org.spicefactory.parsley.messaging.receiver.MessageReceiverInfo;

/**
 * Default implementation of the CommandObserver interface.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class DefaultCommandObserver extends AbstractMethodReceiver implements CommandObserver {

	private final MessageReceiverKind kind;
	private final boolean supportsResult;
	private final boolean supportsNestedCommands;

	private int maxParams;
	private boolean isInterceptor;

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	DefaultCommandObserver(MessageReceiverInfo info, MessageReceiverKind kind) {
		this(info, kind, true, false);
	}

	DefaultCommandObserver(MessageReceiverInfo info, MessageReceiverKind kind, boolean supportsResult) {
		this(info, kind, supportsResult, false);

	}

	DefaultCommandObserver(MessageReceiverInfo info, MessageReceiverKind kind, boolean supportsResult, boolean supportsNestedCommands) {
		super(info);

		this.kind = kind;
		this.supportsResult = supportsResult;
		this.supportsNestedCommands = supportsNestedCommands;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	@Override
	public void init(Provider<?> provider, Method method) {
		super.init(provider, method);

		deduceMessageType();
		deduceSelector();

		Class<?>[] params = targetMethod.getParameterTypes();
		isInterceptor = params.length > 0 && params[params.length - 1].isAssignableFrom(MessageProcessor.class);
	}

	private void deduceMessageType() {
		maxParams = supportsResult ? 4 : 3;
		Class<?>[] params = targetMethod.getParameterTypes();
		if (params.length > maxParams) {
			throw new ContextError("Target " + targetMethod + ": At most " + maxParams
					+ " parameter(s) allowed for this type of Command Observer.");
		}
		int index = supportsResult ? 1 : 0;
		if (params.length >= index + 1) {
			deduceMessageTypeFromParameter(targetMethod, index);
		}
	}

	private void deduceSelector() {
		int index = supportsResult ? 2 : 1;
		Class<?>[] params = targetMethod.getParameterTypes();
		if (params.length >= index + 1) {
			Class<?> param = params[index];
			//			if (!parameter.isAssignableFrom(MessageProcessor.class)) {
			//				selector = parameter.getClass();
			//			}
		}

	}

	@Override
	public int getOrder() {
		if (order != Integer.MAX_VALUE) {
			return order;
		}
		return isInterceptor ? Integer.MIN_VALUE + 1 : Integer.MAX_VALUE;
	}

	@Override
	public void observeCommand(CommandObserverProcessor processor) {
		if ((!processor.isRoot() && !supportsNestedCommands) || (processor.getCommand() instanceof CommandAdapter) && supportsNestedCommands)
			return;

		try {
			Class<?>[] paramTypes = targetMethod.getParameterTypes();
			List<Object> params = new ArrayList<Object>(paramTypes.length);
			if (paramTypes.length >= 1 && maxParams == 4) {
				if (!addResult(params, processor.getResult()))
					return;
			}
			if (paramTypes.length >= maxParams - 2) {
				params.add(processor.getMessage().getInstance());
			}
			if (paramTypes.length >= maxParams - 1) {
				Class<?> param = paramTypes[params.size()];
				if (param.isAssignableFrom(MessageProcessor.class)) {
					params.add(processor);
				} else {
					//				if (processor.getMessage().getSelector() instanceof Class) {
					//					params.add(null);
					//				}
					params.add(processor.getMessage().getSelector());
					if (paramTypes.length == maxParams) {
						params.add(processor);
					}
				}
			}
			targetMethod.setAccessible(true);
			targetMethod.invoke(objectProvider.get(), params.toArray());
		}
		catch (Exception e) {
			throw new Error(e);
		}
	}

	@Override
	public MessageReceiverKind getKind() {
		return kind;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	private boolean addResult(List<Object> params, Object result) {
		return true;
	}
}
