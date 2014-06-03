package org.spicefactory.parsley.core.messaging.impl;

import org.spicefactory.parsley.core.command.ObservableCommand;
import org.spicefactory.parsley.core.messaging.Message;
import org.spicefactory.parsley.core.messaging.MessageReceiverCache;
import org.spicefactory.parsley.core.messaging.MessageRouter;

public class DefaultMessageRouter implements MessageRouter {

	@Override
	public void dispatchMessage(Message message, MessageReceiverCache cache) {
		DefaultMessageProcessor processor = new DefaultMessageProcessor(message, cache);
		processor.start();
	}

	@Override
	public void observeCommand(ObservableCommand command, MessageReceiverCache typeCache, MessageReceiverCache triggerCache) {
		// TODO Auto-generated method stub

	}

}
