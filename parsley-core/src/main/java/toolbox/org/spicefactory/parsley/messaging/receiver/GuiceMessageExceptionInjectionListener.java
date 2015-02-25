package org.spicefactory.parsley.messaging.receiver;

import java.lang.reflect.Method;

import javax.inject.Provider;

import org.spicefactory.parsley.core.context.provider.ObjectProvider;
import org.spicefactory.parsley.core.scope.Scope;
import org.spicefactory.parsley.core.scope.ScopeManager;
import org.spicefactory.parsley.messaging.annotation.MessageException;

import com.google.inject.spi.InjectionListener;

public class GuiceMessageExceptionInjectionListener implements InjectionListener<Object> {

	private final Method method;
	private final MessageException methodInfo;
	private final Provider<ScopeManager> scopeManager;

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	public GuiceMessageExceptionInjectionListener(Provider<ScopeManager> scopeManager, Method method) {
		this.method = method;
		this.methodInfo = method.getAnnotation(MessageException.class);
		this.scopeManager = scopeManager;
	}

	/**
	 * Registers a method which is annotated by MessageHandler.
	 */
	@Override
	public void afterInjection(Object injectee) {
		final ObjectProvider provider = new ObjectProvider(injectee);
		final DefaultMessageExceptionHandler target =
				new DefaultMessageExceptionHandler(new MessageReceiverInfo(methodInfo.type(), methodInfo.selector(), methodInfo.order()),
						methodInfo.exceptionType());
		target.init(provider, method);

		Scope scope = scopeManager.get().getScope(methodInfo.scope());
		scope.getMessageReceivers().addExceptionHandler(target);
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

}
