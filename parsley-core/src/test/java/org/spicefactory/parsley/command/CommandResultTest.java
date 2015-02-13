package org.spicefactory.parsley.command;

import org.junit.Test;
import org.spicefactory.parsley.command.observer.CommandObserversImmediate;
import org.spicefactory.parsley.context.ContextBuilder;
import org.spicefactory.parsley.core.context.Context;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class CommandResultTest {

	private Context context;

	@Test
	public void testImmediate() {
		CommandObserversImmediate observers = new CommandObserversImmediate();
		context = ContextBuilder.newBuilder().config(processor).build();
	}

}
