package org.spicefactory.parsley.messaging.model;

import javax.inject.Singleton;

import org.spicefactory.parsley.messaging.annotation.MessageHandler;
import org.spicefactory.parsley.messaging.messages.TestEvent;
import org.spicefactory.parsley.messaging.messages.TestMessage;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
@Singleton
public class TestMessageHandlersAnnotation extends TestMessageHandlers {

	@Override
	@MessageHandler
	public void allTestMessages(TestMessage message) {
		super.allTestMessages(message);
	}

	@Override
	@MessageHandler(selector = TestEvent.TEST1)
	public void event1(TestMessage message) {
		super.event1(message);
	}

	@Override
	@MessageHandler(selector = TestEvent.TEST2)
	public void event2(TestMessage message) {
		super.event2(message);
	}
}
