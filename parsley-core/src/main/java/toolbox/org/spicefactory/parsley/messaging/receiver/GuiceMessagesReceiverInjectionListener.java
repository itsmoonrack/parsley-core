package org.spicefactory.parsley.messaging.receiver;

import java.lang.reflect.Method;

import javax.inject.Provider;

import org.spicefactory.parsley.core.context.provider.ObjectProvider;
import org.spicefactory.parsley.core.scope.Scope;
import org.spicefactory.parsley.core.scope.ScopeManager;
import org.spicefactory.parsley.messaging.annotation.MessageHandler;
import org.spicefactory.parsley.messaging.annotation.MessageHandlers;

import com.google.inject.spi.InjectionListener;

public class GuiceMessagesReceiverInjectionListener implements InjectionListener<Object> {

	private final Method method;
	private final MessageHandlers methodInfos;
	private final Provider<ScopeManager> scopeManager;

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	public GuiceMessagesReceiverInjectionListener(Provider<ScopeManager> scopeManager, Method method) {
		this.method = method;
		this.methodInfos = method.getAnnotation(MessageHandlers.class);
		this.scopeManager = scopeManager;
	}

	/**
	 * Registers a method which is annotated by MessageHandlers.
	 */
	@Override
	public void afterInjection(Object injectee) {
		final ObjectProvider provider = new ObjectProvider(injectee);
		for (MessageHandler methodInfo : methodInfos.value()) {
			final DefaultMessageHandler target =
					new DefaultMessageHandler(new MessageReceiverInfo(methodInfo.type(), methodInfo.selector(), methodInfo.order()),
							methodInfo.messagesProperties());
			target.init(provider, method);

			Scope scope = scopeManager.get().getScope(methodInfo.scope());
			scope.getMessageReceivers().addTarget(target);
		}
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

}
