package org.spicefactory.parsley.messaging.receiver;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

import javax.inject.Provider;

import org.spicefactory.parsley.core.scope.Scope;
import org.spicefactory.parsley.core.scope.ScopeManager;
import org.spicefactory.parsley.messaging.annotation.MessageHandler;

import com.google.inject.spi.InjectionListener;

public class GuiceMessageReceiverInjectionListener implements InjectionListener<Object> {

	private final Method method;
	private final MessageHandler methodInfo;
	private final Provider<ScopeManager> scopeManager;

	public GuiceMessageReceiverInjectionListener(Provider<ScopeManager> scopeManager, Method method) {
		this.method = method;
		this.methodInfo = method.getAnnotation(MessageHandler.class);
		this.scopeManager = scopeManager;
	}

	/**
	 * Registers a method which is annotated by MessageHandler.
	 */
	@Override
	public void afterInjection(Object injectee) {
		final Provider<Object> provider = new ObjectProvider(injectee);
		final DefaultMessageHandler target = new DefaultMessageHandler(methodInfo);
		target.init(provider, method);

		Scope scope = scopeManager.get().getScope(methodInfo.scope());
		scope.messageReceivers().addTarget(target);
	}

	private class ObjectProvider extends WeakReference<Object> implements Provider<Object> {

		public ObjectProvider(Object reference) {
			super(reference);
		}

	}
}
