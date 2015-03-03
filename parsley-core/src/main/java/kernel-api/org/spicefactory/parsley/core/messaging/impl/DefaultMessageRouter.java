package org.spicefactory.parsley.core.messaging.impl;

import org.spicefactory.parsley.core.command.ObservableCommand;
import org.spicefactory.parsley.core.messaging.Message;
import org.spicefactory.parsley.core.messaging.MessageReceiverCache;
import org.spicefactory.parsley.core.messaging.MessageRouter;
import org.spicefactory.parsley.core.messaging.MessageSettings;

public class DefaultMessageRouter implements MessageRouter {

	// TODO: Add MessageSettings here.
	protected MessageSettings settings = DefaultMessageSettings.class.getAnnotation(MessageSettings.class);

	@Override
	public void dispatchMessage(Message<Object> message, MessageReceiverCache cache) {
		DefaultMessageProcessor processor = new DefaultMessageProcessor(message, cache, settings);
		processor.start();
	}

	@Override
	public void observeCommand(ObservableCommand command, MessageReceiverCache typeCache, MessageReceiverCache triggerCache) {
		DefaultCommandObserverProcessor processor = new DefaultCommandObserverProcessor(command, typeCache, triggerCache, settings);
		processor.start();
	}

	@MessageSettings
	class DefaultMessageSettings {
		// Dummy class.
	}

}
