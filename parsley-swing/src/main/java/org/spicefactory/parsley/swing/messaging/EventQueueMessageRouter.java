package org.spicefactory.parsley.swing.messaging;

import org.spicefactory.parsley.core.messaging.Message;
import org.spicefactory.parsley.core.messaging.MessageReceiverCache;
import org.spicefactory.parsley.core.messaging.MessageRouter;
import org.spicefactory.parsley.core.messaging.impl.DefaultMessageRouter;

public class EventQueueMessageRouter extends DefaultMessageRouter implements MessageRouter {

	@Override
	public void dispatchMessage(Message<Object> message, MessageReceiverCache cache) {
		EventQueueMessageProcessor processor = new EventQueueMessageProcessor(message, cache, settings);
		processor.start();
	}

}
