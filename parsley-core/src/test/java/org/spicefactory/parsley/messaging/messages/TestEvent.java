package org.spicefactory.parsley.messaging.messages;

import org.spicefactory.lib.event.Event;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class TestEvent extends Event {

	public static final int TEST1 = 0x01;
	public static final int TEST2 = 0x02;

	private final int intProp;
	private final String stringProp;

	public TestEvent(int id, String stringProp, int intProp) {
		super(id);
		this.stringProp = stringProp;
		this.intProp = intProp;
	}

	public String getStringProp() {
		return stringProp;
	}

	public int getIntProp() {
		return intProp;
	}

	@Override
	public String toString() {
		return "[TestEvent(" + (getID() == TEST1 ? "TEST1" : "TEST2") + ")]";
	}
}
