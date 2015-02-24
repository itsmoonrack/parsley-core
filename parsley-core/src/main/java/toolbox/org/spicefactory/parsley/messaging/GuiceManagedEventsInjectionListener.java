package org.spicefactory.parsley.messaging;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Provider;

import org.spicefactory.lib.event.EventListener;
import org.spicefactory.lib.event.IEventDispatcher;
import org.spicefactory.lib.event.annotation.Event;
import org.spicefactory.lib.event.annotation.Events;
import org.spicefactory.parsley.core.errors.ContextError;
import org.spicefactory.parsley.core.scope.ScopeManager;
import org.spicefactory.parsley.messaging.annotation.ManagedEvents;

import com.google.inject.spi.InjectionListener;

/**
 * Injection listener that registers listeners to managed events that should be dispatched through Parsley's central message router.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class GuiceManagedEventsInjectionListener implements InjectionListener<Object> {

	private final Class<?> type;
	private final Set<Integer> events;
	private final ManagedEvents typeInfo;
	private final Provider<ScopeManager> scopeManager;

	public GuiceManagedEventsInjectionListener(Provider<ScopeManager> scopeManager, Class<?> type) {
		this.type = type;
		this.typeInfo = type.getAnnotation(ManagedEvents.class);
		this.events = getManagedEvents();
		this.scopeManager = scopeManager;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void afterInjection(final Object injectee) {
		for (int type : events) {
			((IEventDispatcher<EventListener<org.spicefactory.lib.event.Event>>) injectee).addEventListener(type,
					new EventListener<org.spicefactory.lib.event.Event>() {
						@Override
						public void process(org.spicefactory.lib.event.Event event) {
							MessageDispatcher dispatcher =
									new org.spicefactory.parsley.messaging.MessageDispatcher(scopeManager.get(), typeInfo.scope(), injectee);
							dispatcher.dispatchMessage(event);
						}
					});
		}
	}

	private Set<Integer> getManagedEvents() {
		Set<Integer> events = new HashSet<Integer>();

		for (final int id : type.getAnnotation(ManagedEvents.class).value()) {
			addEvent(events, new Event() {

				@Override
				public Class<? extends Annotation> annotationType() {
					return Event.class;
				}

				@Override
				public Class<? extends org.spicefactory.lib.event.Event> type() {
					return org.spicefactory.lib.event.Event.class;
				}

				@Override
				public int id() {
					return id;
				}
			});
		}

		if (events.isEmpty()) {
			if (type.isAnnotationPresent(Events.class)) {
				for (Event event : type.getAnnotation(Events.class).value()) {
					addEvent(events, event);
				}
			} else if (type.isAnnotationPresent(Event.class)) {
				addEvent(events, type.getAnnotation(Event.class));
			}
		}

		if (events.isEmpty()) {
			throw new ContextError("No event values specified in @ManagedEvents annotation and no @Events/@Event annotation on " + type);
		}

		return events;
	}

	private void addEvent(Set<Integer> events, Event event) {
		if (!events.add(event.id()))
			throw new ContextError("Event values " + event.id() + " clash on " + type);
	}

}
