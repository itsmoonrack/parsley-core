package org.spicefactory.parsley.messaging.model;

import javax.inject.Singleton;

import org.spicefactory.lib.event.Event;
import org.spicefactory.parsley.messaging.annotation.MessageHandler;
import org.spicefactory.parsley.messaging.messages.TestEvent;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
@Singleton
public class FaultyMessageHandlersAnnotation extends FaultyMessageHandlers {

	@Override
	@MessageHandler
	public void allTestEvents(TestEvent event) {
		super.allTestEvents(event);
	}

	@Override
	@MessageHandler
	public void allEvents(Event event) {
		super.allEvents(event);
	}

	@Override
	@MessageHandler(selector = TestEvent.TEST1)
	public void event1(TestEvent event) {
		super.event1(event);
	}

	@Override
	@MessageHandler(selector = TestEvent.TEST2)
	public void event2(TestEvent event) {
		super.event2(event);
	}

}
