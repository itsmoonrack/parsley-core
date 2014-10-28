package org.spicefactory.parsley.guice.messaging;

import java.lang.reflect.Field;

import javax.inject.Provider;

import org.spicefactory.parsley.core.scope.ScopeManager;
import org.spicefactory.parsley.messaging.annotation.MessageDispatcher;

import com.google.inject.MembersInjector;

class GuiceMessageDispatcherMembersInjector<T> implements MembersInjector<T> {

	private final Field field;
	private final MessageDispatcher fieldInfo;
	private final Provider<ScopeManager> scopeManager;

	GuiceMessageDispatcherMembersInjector(Provider<ScopeManager> scopeManager, Field field) {
		this.field = field;
		this.fieldInfo = field.getAnnotation(MessageDispatcher.class);
		this.scopeManager = scopeManager;
	}

	@Override
	public void injectMembers(T instance) {
		try {
			field.setAccessible(true);
			field.set(instance, new org.spicefactory.parsley.messaging.MessageDispatcher(scopeManager.get(), fieldInfo.scope(), instance));
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
