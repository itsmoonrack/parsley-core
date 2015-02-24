package org.spicefactory.parsley.messaging.model;

import org.spicefactory.parsley.core.messaging.MessageProcessor;
import org.spicefactory.parsley.messaging.messages.TestEvent;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */

public class MessageInterceptors {

	public int genericEventCount = 0, test1Count = 0, test2Count = 0;
	private MessageProcessor<?> event2Processor;

	public void interceptAllMessages(TestEvent message, MessageProcessor<TestEvent> processor) {
		if (processor.getMessage().getInstance().getID() == TestEvent.TEST1) {
			test1Count++;
		} else if (processor.getMessage().getInstance().getID() == TestEvent.TEST2) {
			test2Count++;
		} else {
			throw new IllegalArgumentException("Unexpected event type: " + processor.getMessage().getClass());
		}
	}

	public void allEvent(Object message, MessageProcessor<Object> processor) {
		genericEventCount++;
	}

	public void event1(TestEvent message, MessageProcessor<TestEvent> processor) {
		if (processor.getMessage().getInstance().getID() == TestEvent.TEST1) {
			test1Count++;
		} else {
			throw new IllegalArgumentException("Unexpected event type: " + processor.getMessage().getClass());
		}
	}

	public void event2(TestEvent message, MessageProcessor<TestEvent> processor) {
		processor.suspend();
		if (processor.getMessage().getInstance().getID() == TestEvent.TEST2) {
			test2Count++;
		} else {
			throw new IllegalArgumentException("Unexpected event type: " + processor.getMessage().getClass());
		}
		event2Processor = processor;
	}

	public void proceedEvent2() {
		event2Processor.resume();
	}

}
