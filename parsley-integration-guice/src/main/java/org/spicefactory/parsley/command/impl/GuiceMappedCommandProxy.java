package org.spicefactory.parsley.command.impl;

import org.spicefactory.lib.command.data.DefaultCommandData;
import org.spicefactory.parsley.core.command.ManagedCommandFactory;
import org.spicefactory.parsley.core.command.ManagedCommandProxy;
import org.spicefactory.parsley.core.messaging.MessageProcessor;
import org.spicefactory.parsley.core.messaging.receiver.MessageTarget;
import org.spicefactory.parsley.core.scope.ScopeManager;
import org.spicefactory.parsley.messaging.annotation.MessageHandler;
import org.spicefactory.parsley.messaging.receiver.AbstractMessageReceiver;

/**
 * Parsley message target that executes a command when a message is received.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public class GuiceMappedCommandProxy extends AbstractMessageReceiver implements MessageTarget {

	private final ScopeManager scopeManager;
	private final ManagedCommandFactory factory;

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	GuiceMappedCommandProxy(ManagedCommandFactory factory, ScopeManager scopeManager, MessageHandler info) {
		super(info);
		this.factory = factory;
		this.scopeManager = scopeManager;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	@Override
	public void handleMessage(MessageProcessor processor) {
		ManagedCommandProxy command = factory.newInstance();
		DefaultCommandData data = new DefaultCommandData();
		data.addValue(processor.message().instance());
		command.prepare(new GuiceManagedCommandLifecycle(scopeManager, command, processor.message()), data);
		try {
			command.execute();
		}
		catch (Exception e) {
			return;
		}
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////
}
