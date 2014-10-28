package org.spicefactory.parsley.core.messaging.impl;

import org.spicefactory.parsley.core.command.ObservableCommand;
import org.spicefactory.parsley.core.messaging.Message;
import org.spicefactory.parsley.core.messaging.MessageReceiverCache;
import org.spicefactory.parsley.core.messaging.MessageRouter;
import org.spicefactory.parsley.core.messaging.MessageSettings;

class DefaultMessageRouter implements MessageRouter {

	// TODO: Add MessageSettings here.
	MessageSettings settings = null;

	@Override
	public void dispatchMessage(Message message, MessageReceiverCache cache) {
		DefaultMessageProcessor processor = new DefaultMessageProcessor(message, cache, settings);
		processor.start();
	}

	@Override
	public void observeCommand(ObservableCommand command, MessageReceiverCache typeCache, MessageReceiverCache triggerCache) {
		DefaultCommandObserverProcessor processor = new DefaultCommandObserverProcessor(command, typeCache, triggerCache, settings);
		processor.start();
	}

}
