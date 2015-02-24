package org.spicefactory.parsley.messaging.model;

import javax.inject.Singleton;

import org.spicefactory.lib.event.Event;
import org.spicefactory.parsley.messaging.annotation.MessageHandler;
import org.spicefactory.parsley.messaging.messages.TestEvent;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
@Singleton
public class MessageHandlers {

	public int genericEventCount = 0, test1Count = 0, test2Count = 0;

	public int intProp = 0;
	public String stringProp = "";

	public String order = "";

	@MessageHandler(order = 3)
	public void allTestEvents(TestEvent event) {
		order += "T";
		if (event.getID() == TestEvent.TEST1) {
			test1Count++;
		} else if (event.getID() == TestEvent.TEST2) {
			test2Count++;
		} else {
			throw new IllegalArgumentException("Unexpected event id: " + event.getID());
		}
	}

	@MessageHandler(order = 2)
	public void allEvents(Event event) {
		order += "A";
		genericEventCount++;
	}

	@MessageHandler(order = 1, selector = TestEvent.TEST1)
	public void event1(TestEvent event) {
		order += "1";
		if (event.getID() == TestEvent.TEST1) {
			test1Count++;
		} else {
			throw new IllegalArgumentException("Unexpected event id: " + event.getID());
		}
	}

	@MessageHandler(order = 1, selector = TestEvent.TEST2)
	public void event2(TestEvent event) {
		order += "2";
		if (event.getID() == TestEvent.TEST2) {
			test2Count++;
		} else {
			throw new IllegalArgumentException("Unexpected event id: " + event.getID());
		}
	}

	@MessageHandler(type = TestEvent.class, messagesProperties = {"stringProp", "intProp"})
	public void mappedProperties(String stringProp, int intProp) {
		this.stringProp = stringProp;
		this.intProp = intProp;
	}
}
