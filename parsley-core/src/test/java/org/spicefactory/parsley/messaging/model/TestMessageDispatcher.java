package org.spicefactory.parsley.messaging.model;

import org.spicefactory.parsley.messaging.Dispatcher;
import org.spicefactory.parsley.messaging.annotation.MessageDispatcher;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class TestMessageDispatcher {

	@MessageDispatcher
	private Dispatcher dispatcher;

	public void dispatchMessage(Object message) {
		dispatcher.dispatchMessage(message);
	}
}
