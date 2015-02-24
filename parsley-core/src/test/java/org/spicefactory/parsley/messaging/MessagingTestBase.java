package org.spicefactory.parsley.messaging;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.spicefactory.parsley.util.Matchers.contextInState;

import org.junit.Before;
import org.junit.Test;
import org.spicefactory.lib.event.Event;
import org.spicefactory.parsley.core.context.Context;
import org.spicefactory.parsley.messaging.messages.TestEvent;
import org.spicefactory.parsley.messaging.model.EventSource;
import org.spicefactory.parsley.messaging.model.MessageHandlers;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public abstract class MessagingTestBase {

	private Context context;

	@Before
	public void createContext() {
		context = messagingContext();
	}

	@Test
	public void testContextState() {
		assertThat(context, contextInState());
	}

	@Test
	public void testMessageHandlers() {
		// Given
		EventSource source = context.getInstance(EventSource.class);
		MessageHandlers handlers = context.getInstance(MessageHandlers.class);

		// When
		source.dispatchEvent(new TestEvent(TestEvent.TEST1, "foo1", 7));
		source.dispatchEvent(new TestEvent(TestEvent.TEST2, "foo2", 9));
		source.dispatchEvent(new Event(1));

		// Then
		assertThat(handlers.test1Count, equalTo(2));
		assertThat(handlers.test2Count, equalTo(2));
		assertThat(handlers.genericEventCount, equalTo(3));
		assertThat(handlers.stringProp, equalTo("foo2"));
		assertThat(handlers.intProp, equalTo(9));
	}

	@Test
	public void testMessageHandlerOrder() {
		// Given
		EventSource source = context.getInstance(EventSource.class);
		MessageHandlers handlers = context.getInstance(MessageHandlers.class);

		// When
		source.dispatchEvent(new TestEvent(TestEvent.TEST1, "foo1", 7));
		source.dispatchEvent(new TestEvent(TestEvent.TEST2, "foo2", 9));
		source.dispatchEvent(new Event(1));

		// Then
		assertThat(handlers.order, equalTo("1AT2ATA"));
	}

	@Test
	public void testMessageInterceptors() {
		EventSource source = context.getInstance(EventSource.class);
		MessageHandlers handlers = context.getInstance(MessageHandlers.class);

	}

	protected abstract Context messagingContext();

}
