package org.spicefactory.parsley.messaging.model;

import javax.inject.Singleton;

import org.spicefactory.lib.event.Event;
import org.spicefactory.parsley.messaging.annotation.MessageHandler;
import org.spicefactory.parsley.messaging.messages.TestEvent;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
@Singleton
public class MessageHandlersAnnotation extends MessageHandlers {

	@Override
	@MessageHandler(order = 3)
	public void allTestEvents(TestEvent event) {
		super.allTestEvents(event);
	}

	@Override
	@MessageHandler(order = 2)
	public void allEvents(Event event) {
		super.allEvents(event);
	}

	@Override
	@MessageHandler(order = 1, selector = TestEvent.TEST1)
	public void event1(TestEvent event) {
		super.event1(event);
	}

	@Override
	@MessageHandler(order = 1, selector = TestEvent.TEST2)
	public void event2(TestEvent event) {
		super.event2(event);
	}

	@Override
	@MessageHandler(type = TestEvent.class, messagesProperties = {"stringProp", "intProp"})
	public void mappedProperties(String stringProp, int intProp) {
		super.mappedProperties(stringProp, intProp);
	}
}
