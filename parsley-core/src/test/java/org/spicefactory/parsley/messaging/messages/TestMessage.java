package org.spicefactory.parsley.messaging.messages;

import org.spicefactory.parsley.core.messaging.Selector;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class TestMessage {

	@Selector
	public int name;

	public int value;

	public TestMessage(int name, int value) {
		this.name = name;
		this.value = value;
	}

}
