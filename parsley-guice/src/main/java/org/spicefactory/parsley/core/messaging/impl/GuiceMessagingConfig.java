package org.spicefactory.parsley.core.messaging.impl;

import org.spicefactory.parsley.core.messaging.MessageReceiverRegistry;
import org.spicefactory.parsley.core.messaging.MessageRouter;

import com.google.inject.AbstractModule;

public class GuiceMessagingConfig extends AbstractModule {

	@Override
	protected void configure() {
		bind(MessageReceiverRegistry.class).to(DefaultMessageReceiverRegistry.class);
		bind(MessageRouter.class).to(DefaultMessageRouter.class);
	}
}
