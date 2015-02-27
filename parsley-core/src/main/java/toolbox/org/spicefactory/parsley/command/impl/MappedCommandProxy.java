package org.spicefactory.parsley.command.impl;

import org.spicefactory.lib.command.data.DefaultCommandData;
import org.spicefactory.parsley.core.command.ManagedCommandFactory;
import org.spicefactory.parsley.core.command.ManagedCommandProxy;
import org.spicefactory.parsley.core.context.Context;
import org.spicefactory.parsley.core.messaging.MessageProcessor;
import org.spicefactory.parsley.core.messaging.receiver.MessageTarget;
import org.spicefactory.parsley.messaging.receiver.AbstractMessageReceiver;
import org.spicefactory.parsley.messaging.receiver.MessageReceiverInfo;

/**
 * Parsley message target that executes a command when a message is received.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class MappedCommandProxy extends AbstractMessageReceiver implements MessageTarget {

	private final Context context;
	private final ManagedCommandFactory factory;

	/////////////////////////////////////////////////////////////////////////////
	// Component exposed API.
	/////////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a new instance.
	 * @param factory the factory to use for creating new commands
	 * @param context the Context that should manage all commands executed by this proxy
	 * @param messageType the type of message that should trigger command execution
	 * @param selector the optional selector to match messages
	 * @param order the execution order for this receiver compared to other handlers for the same message
	 */
	public MappedCommandProxy(ManagedCommandFactory factory, Context context, MessageReceiverInfo info) {
		super(info);

		this.context = context;
		this.factory = factory;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	@Override
	public void handleMessage(MessageProcessor<Object> processor) {
		ManagedCommandProxy command = factory.newInstance();
		DefaultCommandData data = new DefaultCommandData();
		data.addValue(processor.getMessage().getInstance());
		command.prepare(new ManagedCommandLifecycle(context, command, processor.getMessage()), data);
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
