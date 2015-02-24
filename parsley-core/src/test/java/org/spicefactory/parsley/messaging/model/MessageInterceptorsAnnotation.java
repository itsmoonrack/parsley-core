package org.spicefactory.parsley.messaging.model;

import javax.inject.Singleton;

import org.spicefactory.parsley.core.messaging.MessageProcessor;
import org.spicefactory.parsley.messaging.annotation.MessageHandler;
import org.spicefactory.parsley.messaging.messages.TestEvent;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
@Singleton
public class MessageInterceptorsAnnotation extends MessageInterceptors {

	@Override
	@MessageHandler(order = -1)
	public void interceptAllMessages(TestEvent message, MessageProcessor<TestEvent> processor) {
		super.interceptAllMessages(message, processor);
	}

	@Override
	@MessageHandler(order = -1)
	public void allEvent(Object message, MessageProcessor<Object> processor) {
		super.allEvent(message, processor);
	}

	@Override
	@MessageHandler(selector = TestEvent.TEST1)
	public void event1(TestEvent message, MessageProcessor<TestEvent> processor) {
		super.event1(message, processor);
	}

	@Override
	@MessageHandler(selector = TestEvent.TEST2)
	public void event2(TestEvent message, MessageProcessor<TestEvent> processor) {
		super.event2(message, processor);
	}

}
