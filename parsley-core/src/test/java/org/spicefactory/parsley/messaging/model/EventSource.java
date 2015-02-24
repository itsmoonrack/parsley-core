package org.spicefactory.parsley.messaging.model;

import org.spicefactory.parsley.messaging.Dispatcher;
import org.spicefactory.parsley.messaging.annotation.MessageDispatcher;

// TODO: Make this work auto-magically when extending EventDispatcher.
public class EventSource /* extends EventDispatcher */{

	@MessageDispatcher
	private Dispatcher dispatcher;

	public void dispatchEvent(Object event) {
		dispatcher.dispatchMessage(event);
	}

}
