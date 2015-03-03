package org.spicefactory.parsley.config;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.inject.Singleton;

import org.spicefactory.parsley.comobserver.annotation.CommandException;
import org.spicefactory.parsley.comobserver.annotation.CommandResult;
import org.spicefactory.parsley.comobserver.annotation.CommandStatus;
import org.spicefactory.parsley.comobserver.receiver.GuiceCommandExceptionInjectionListener;
import org.spicefactory.parsley.comobserver.receiver.GuiceCommandResultInjectionListener;
import org.spicefactory.parsley.comobserver.receiver.GuiceCommandStatusInjectionListener;
import org.spicefactory.parsley.core.command.CommandManager;
import org.spicefactory.parsley.core.scope.ScopeManager;
import org.spicefactory.parsley.messaging.GuiceManagedEventsInjectionListener;
import org.spicefactory.parsley.messaging.GuiceMessageDispatcherMembersInjector;
import org.spicefactory.parsley.messaging.annotation.ManagedEvents;
import org.spicefactory.parsley.messaging.annotation.MessageDispatcher;
import org.spicefactory.parsley.messaging.annotation.MessageException;
import org.spicefactory.parsley.messaging.annotation.MessageHandler;
import org.spicefactory.parsley.messaging.annotation.MessageHandlers;
import org.spicefactory.parsley.messaging.receiver.GuiceMessageExceptionInjectionListener;
import org.spicefactory.parsley.messaging.receiver.GuiceMessageReceiverInjectionListener;
import org.spicefactory.parsley.messaging.receiver.GuiceMessagesReceiverInjectionListener;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

public class GuiceParsleyTypeListener implements TypeListener {

	@Override
	public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
		Class<? super I> c = type.getRawType();

		// Registers an injection listener on events managed by this class
		// so Parsley central router can dispatch them through framework.
		if (c.isAnnotationPresent(ManagedEvents.class)) {
			encounter.register(new GuiceManagedEventsInjectionListener(encounter.getProvider(ScopeManager.class), c));
		}

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
			if (type.getRawType().isAnnotationPresent(Singleton.class)) {
				for (Method method : c.getDeclaredMethods()) {
					if (method.isAnnotationPresent(MessageException.class)) {
						encounter.register(new GuiceMessageExceptionInjectionListener(encounter.getProvider(ScopeManager.class), method));
					}
					if (method.isAnnotationPresent(MessageHandler.class)) {
						encounter.register(new GuiceMessageReceiverInjectionListener(encounter.getProvider(ScopeManager.class), method));
					}
					if (method.isAnnotationPresent(MessageHandlers.class)) {
						encounter.register(new GuiceMessagesReceiverInjectionListener(encounter.getProvider(ScopeManager.class), method));
					}
					if (method.isAnnotationPresent(CommandStatus.class)) {
						encounter.register(new GuiceCommandStatusInjectionListener(encounter.getProvider(ScopeManager.class), encounter
								.getProvider(CommandManager.class), method));
					}
					if (method.isAnnotationPresent(CommandResult.class)) {
						encounter.register(new GuiceCommandResultInjectionListener(encounter.getProvider(ScopeManager.class), method));
					}
					if (method.isAnnotationPresent(CommandException.class)) {
						encounter.register(new GuiceCommandExceptionInjectionListener(encounter.getProvider(ScopeManager.class), method));
					}
				}
			}
			c = c.getSuperclass();
		}
	}

}
