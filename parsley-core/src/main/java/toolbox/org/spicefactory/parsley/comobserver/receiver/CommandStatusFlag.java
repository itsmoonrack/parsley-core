package org.spicefactory.parsley.comobserver.receiver;

import org.spicefactory.parsley.core.command.CommandManager;
import org.spicefactory.parsley.core.command.CommandObserverProcessor;
import org.spicefactory.parsley.core.messaging.MessageReceiverKind;
import org.spicefactory.parsley.core.messaging.receiver.CommandObserver;
import org.spicefactory.parsley.messaging.receiver.AbstractMethodReceiver;
import org.spicefactory.parsley.messaging.receiver.MessageReceiverInfo;

public class CommandStatusFlag extends AbstractMethodReceiver implements CommandObserver {

	private final CommandManager manager;
	private final MessageReceiverKind kind;

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	public CommandStatusFlag(MessageReceiverInfo info, MessageReceiverKind kind, CommandManager manager) {
		super(info);

		this.kind = kind;
		this.manager = manager;
		this.info.order = Integer.MIN_VALUE;
	}

	@Override
	public MessageReceiverKind getKind() {
		return kind;
	}

	@Override
	public void observeCommand(CommandObserverProcessor processor) {
		if (!processor.isRoot())
			return;

		try {
			targetMethod.invoke(provider.get(), manager.hasActiveCommandsForTrigger(info.type, info.selector));
		}
		catch (Exception e) {
			throw new Error(e);
		}
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////
}
