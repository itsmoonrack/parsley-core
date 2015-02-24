package org.spicefactory.parsley.messaging.model;

import org.spicefactory.parsley.messaging.messages.TestEvent;
import org.spicefactory.parsley.messaging.messages.TestMessage;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class TestMessageHandlers {

	public int test1Count = 0, test2Count = 0, sum = 0;

	public void allTestMessages(TestMessage message) {
		if (message.name == TestEvent.TEST1) {
			test1Count++;
		} else if (message.name == TestEvent.TEST2) {
			test2Count++;
		} else {
			throw new IllegalArgumentException("Unexpected event id: "+message.name);
		}
		sum += message.value;
	}

	public void event1(TestMessage message) {
		if (message.name == TestEvent.TEST1) {
			test1Count++;
		} else {
			throw new IllegalArgumentException("Unexpected event id: "+message.name);
		}
		sum += message.value;
	}

	public void event2(TestMessage message) {
		if (message.name == TestEvent.TEST2) {
			test2Count++;
		} else {
			throw new IllegalArgumentException("Unexpected event id: "+message.name);
		}
		sum += message.value;
	}

}
