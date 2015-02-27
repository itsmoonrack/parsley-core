package org.spicefactory.parsley.comobserver.receiver;

import java.lang.reflect.Method;

import org.spicefactory.parsley.comobserver.annotation.CommandException;
import org.spicefactory.parsley.core.context.provider.ObjectProvider;
import org.spicefactory.parsley.core.messaging.MessageReceiverKind;
import org.spicefactory.parsley.core.scope.Scope;
import org.spicefactory.parsley.core.scope.ScopeManager;
import org.spicefactory.parsley.messaging.receiver.MessageReceiverInfo;

import com.google.inject.Provider;
import com.google.inject.spi.InjectionListener;

public class GuiceCommandExceptionInjectionListener implements InjectionListener<Object> {

	private final Method method;
	private final CommandException methodInfo;
	private final Provider<ScopeManager> scopeManager;

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	public GuiceCommandExceptionInjectionListener(Provider<ScopeManager> scopeManager, Method method) {
		this.method = method;
		this.methodInfo = method.getAnnotation(CommandException.class);
		this.scopeManager = scopeManager;
	}

	@Override
	public void afterInjection(Object injectee) {
		final ObjectProvider provider = new ObjectProvider(injectee);
		final DefaultCommandObserver observer =
				new DefaultCommandObserver(new MessageReceiverInfo(methodInfo.type(), methodInfo.selector(), methodInfo.order()),
						MessageReceiverKind.COMMAND_EXCEPTION_BY_TRIGGER);
		observer.init(provider, method);

		Scope scope = scopeManager.get().getScope(methodInfo.scope());
		scope.getMessageReceivers().addCommandObserver(observer);
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

}
