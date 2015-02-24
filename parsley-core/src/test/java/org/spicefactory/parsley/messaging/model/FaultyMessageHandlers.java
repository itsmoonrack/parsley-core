package org.spicefactory.parsley.messaging.model;

import org.spicefactory.lib.event.Event;
import org.spicefactory.parsley.core.errors.ContextError;
import org.spicefactory.parsley.messaging.messages.TestEvent;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class FaultyMessageHandlers {

	public void allTestEvents(TestEvent event) {
		throw new Error("Faulty handler");
	}

	public void allEvents(Event event) {
		throw new Error("Faulty handler");
	}

	public void event1(TestEvent event) {
		throw new ContextError("Faulty handler");
	}

	public void event2(TestEvent event) {
		throw new ContextError("Faulty handler");
	}
}
