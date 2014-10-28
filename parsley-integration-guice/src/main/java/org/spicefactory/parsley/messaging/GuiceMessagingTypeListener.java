package org.spicefactory.parsley.messaging;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.inject.Singleton;

import org.spicefactory.parsley.core.scope.ScopeManager;
import org.spicefactory.parsley.messaging.annotation.MessageDispatcher;
import org.spicefactory.parsley.messaging.annotation.MessageHandler;
import org.spicefactory.parsley.messaging.receiver.GuiceMessageReceiverInjectionListener;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public class GuiceMessagingTypeListener implements TypeListener {

	@Override
	public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
		Class<? super I> c = type.getRawType();
		while (c.getSuperclass() != null) {
			// Registers a members injector for a Dispatcher fields annotated with
			// @MessageDispatcher so Guice can inject an instance of MessageDispatcher.
			for (Field field : c.getDeclaredFields()) {
				if (field.isAnnotationPresent(MessageDispatcher.class)) {
					encounter.register(new GuiceMessageDispatcherMembersInjector<I>(encounter.getProvider(ScopeManager.class), field));
				}
			}
			// Registers an injection listener on methods annotated with @MessageHandler
			// so MessageRouter can dispatch messages to this singleton instance.
			for (Method method : c.getDeclaredMethods()) {
				if (method.isAnnotationPresent(MessageHandler.class) && type.getRawType().isAnnotationPresent(Singleton.class)) {
					encounter.register(new GuiceMessageReceiverInjectionListener(encounter.getProvider(ScopeManager.class), method));
				}

			}
			c = c.getSuperclass();
		}
	}

}
