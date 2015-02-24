package org.spicefactory.parsley.messaging;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.spicefactory.parsley.util.Matchers.contextInState;

import org.junit.Before;
import org.junit.Test;
import org.spicefactory.lib.event.Event;
import org.spicefactory.parsley.core.context.Context;
import org.spicefactory.parsley.messaging.messages.TestEvent;
import org.spicefactory.parsley.messaging.messages.TestMessage;
import org.spicefactory.parsley.messaging.model.EventSource;
import org.spicefactory.parsley.messaging.model.FaultyMessageHandlers;
import org.spicefactory.parsley.messaging.model.MessageHandlers;
import org.spicefactory.parsley.messaging.model.MessageInterceptors;
import org.spicefactory.parsley.messaging.model.TestMessageDispatcher;
import org.spicefactory.parsley.messaging.model.TestMessageHandlers;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public abstract class MessagingTestBase {

	private Context context;
	private boolean lazy;

	protected MessagingTestBase(boolean lazy) {
		this.lazy = lazy;
	}

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
		MessageHandlers handlers = null;

		// When
		if (lazy) handlers = context.getInstance(MessageHandlers.class);
		source.dispatchEvent(new TestEvent(TestEvent.TEST1, "foo1", 7));
		source.dispatchEvent(new TestEvent(TestEvent.TEST2, "foo2", 9));
		source.dispatchEvent(new Event(0x00));
		if (!lazy) handlers = context.getInstance(MessageHandlers.class);

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
		MessageHandlers handlers = null;

		// When
		if (lazy) handlers = context.getInstance(MessageHandlers.class);
		source.dispatchEvent(new TestEvent(TestEvent.TEST1, "foo1", 7));
		source.dispatchEvent(new TestEvent(TestEvent.TEST2, "foo2", 9));
		source.dispatchEvent(new Event(0x00));
		if (!lazy) handlers = context.getInstance(MessageHandlers.class);

		// Then
		assertThat(handlers.order, equalTo("1AT2ATA"));
	}

	// TODO @Test testMessageBindings()

	@Test
	public void testMessageInterceptors() {
		// Given
		EventSource source = context.getInstance(EventSource.class);
		MessageHandlers handlers = context.getInstance(MessageHandlers.class);
		MessageInterceptors interceptors = context.getInstance(MessageInterceptors.class);

		// When
		source.dispatchEvent(new TestEvent(TestEvent.TEST1, "foo1", 7));
		source.dispatchEvent(new TestEvent(TestEvent.TEST2, "foo2", 9));
		source.dispatchEvent(new Event(0x00));

		// Then
		assertThat(handlers.test1Count, equalTo(2));
		assertThat(handlers.test2Count, equalTo(0));
		assertThat(handlers.genericEventCount, equalTo(2));
		assertThat(handlers.stringProp, equalTo("foo1"));
		assertThat(handlers.intProp, equalTo(7));

		assertThat(interceptors.test1Count, equalTo(2));
		assertThat(interceptors.test2Count, equalTo(1));

		// When
		interceptors.proceedEvent2();

		// Then
		assertThat(handlers.test1Count, equalTo(2));
		assertThat(handlers.test2Count, equalTo(2));
		assertThat(handlers.genericEventCount, equalTo(3));
		assertThat(handlers.stringProp, equalTo("foo2"));
		assertThat(handlers.intProp, equalTo(9));

		assertThat(interceptors.test1Count, equalTo(2));
		assertThat(interceptors.test2Count, equalTo(2));
	}

	@Test
	public void testErrorHandlers() {
		// Given
		EventSource source = context.getInstance(EventSource.class);
		context.getInstance(FaultyMessageHandlers.class); // must fetch explicitly - it's lazy

	}

	@Test
	public void testMessageDispatcher() {
		// Given
		TestMessageDispatcher dispatcher = context.getInstance(TestMessageDispatcher.class);
		TestMessageHandlers handlers = null;
		TestMessage m1 = new TestMessage(TestEvent.TEST1, 3);
		TestMessage m2 = new TestMessage(TestEvent.TEST2, 5);

		// When
		if (lazy) handlers = context.getInstance(TestMessageHandlers.class);
		dispatcher.dispatchMessage(m1);
		dispatcher.dispatchMessage(m2);
		if (!lazy) handlers = context.getInstance(TestMessageHandlers.class);

		// Then
		assertThat(handlers.test1Count, equalTo(2));
		assertThat(handlers.test2Count, equalTo(2));
		assertThat(handlers.sum, equalTo(5 + 5 + 3 + 3));
	}

	protected abstract Context messagingContext();

}
