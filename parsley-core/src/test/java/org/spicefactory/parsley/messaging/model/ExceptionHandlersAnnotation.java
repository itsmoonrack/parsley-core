package org.spicefactory.parsley.messaging.model;

import javax.inject.Singleton;

import org.spicefactory.lib.event.Event;
import org.spicefactory.parsley.core.errors.ContextError;
import org.spicefactory.parsley.core.messaging.MessageProcessor;
import org.spicefactory.parsley.messaging.annotation.MessageException;
import org.spicefactory.parsley.messaging.messages.TestEvent;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
@Singleton
public class ExceptionHandlersAnnotation extends ExceptionHandlers {

	@Override
	@MessageException(type = TestEvent.class)
	public void allTestEvents(Error error, MessageProcessor<TestEvent> processor) {
		super.allTestEvents(error, processor);
	}

	@Override
	@MessageException
	public void allEvents(Error error, MessageProcessor<Event> processor) {
		super.allEvents(error, processor);
	}

	@Override
	@MessageException(type = TestEvent.class, selector = TestEvent.TEST1)
	public void event1(ContextError error, MessageProcessor<TestEvent> processor) {
		super.event1(error, processor);
	}

	@Override
	@MessageException(type = TestEvent.class, selector = TestEvent.TEST2)
	public void event2(ContextError error, MessageProcessor<TestEvent> processor) {
		super.event2(error, processor);
	}
}
