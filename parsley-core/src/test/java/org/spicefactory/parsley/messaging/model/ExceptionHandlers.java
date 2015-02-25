package org.spicefactory.parsley.messaging.model;

import org.spicefactory.lib.event.Event;
import org.spicefactory.parsley.core.errors.ContextError;
import org.spicefactory.parsley.core.messaging.MessageProcessor;
import org.spicefactory.parsley.messaging.messages.TestEvent;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class ExceptionHandlers extends MessageCounter {

	public void allTestEvents(Error error, MessageProcessor<TestEvent> processor) {
		addMessage(processor.getMessage().getInstance());
	}

	public void allEvents(Error error, MessageProcessor<Event> processor) {
		addMessage(processor.getMessage().getInstance());
	}

	public void event1(ContextError error, MessageProcessor<TestEvent> processor) {
		addMessage(processor.getMessage().getInstance(), TestEvent.TEST1);
	}

	public void event2(ContextError error, MessageProcessor<TestEvent> processor) {
		addMessage(processor.getMessage().getInstance(), TestEvent.TEST2);
	}

}
