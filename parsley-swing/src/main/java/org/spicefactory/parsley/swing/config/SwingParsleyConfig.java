package org.spicefactory.parsley.swing.config;

import org.spicefactory.lib.command.adapter.CommandAdapters;
import org.spicefactory.lib.command.swing.SwingCommandAdapterFactory;
import org.spicefactory.parsley.core.messaging.MessageRouter;
import org.spicefactory.parsley.swing.messaging.EventQueueMessageRouter;

import com.google.inject.AbstractModule;

public class SwingParsleyConfig extends AbstractModule {

	static {
		// Adds this factory to the registry of command adapters.
		// We consider that it is configuration code hence it is
		// acceptable to make use of static keyword in this case
		// so it remains in the JVM between application restarts.
		CommandAdapters.addFactory(new SwingCommandAdapterFactory(), 0);
	}

	@Override
	protected void configure() {
		bind(MessageRouter.class).to(EventQueueMessageRouter.class);
	}
}
