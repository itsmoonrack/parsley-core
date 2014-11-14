package org.spicefactory.parsley.core.scope.impl;

import org.spicefactory.parsley.core.command.CommandManager;
import org.spicefactory.parsley.core.command.ObservableCommand;
import org.spicefactory.parsley.core.command.impl.GuiceCommandManager;
import org.spicefactory.parsley.core.context.Context;
import org.spicefactory.parsley.core.messaging.MessageReceiverCache;
import org.spicefactory.parsley.core.messaging.MessageReceiverRegistry;
import org.spicefactory.parsley.core.messaging.impl.DefaultMessageReceiverRegistry;
import org.spicefactory.parsley.core.scope.ScopeDefinition;
import org.spicefactory.parsley.core.scope.ScopeInfo;

/**
 * Default implementation of the ScopeInfo interface.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
class GuiceScopeInfo implements ScopeInfo {

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	GuiceScopeInfo(ScopeDefinition definition, CommandManager commandManager /*, BootstrapInfo info*/) {
		this.name = definition.name();
		this.uuid = definition.uuid();
		this.inherited = definition.inherited();
		this.messageReceivers = new DefaultMessageReceiverRegistry();
		//		for (Class<? extends MessageExceptionHandler> handler : info.messageSettings().exceptionHandlers()) {
		//			try {
		//				messageReceivers.addExceptionHandler(handler.newInstance());
		//			}
		//			catch (Exception e) {
		//				e.printStackTrace();
		//			}
		//		}
		this.commandManager = commandManager;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	@Override
	public String name() {
		return name;
	}

	@Override
	public String uuid() {
		return uuid;
	}

	@Override
	public boolean inherited() {
		return inherited;
	}

	@Override
	public Context rootContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageReceiverRegistry messageReceivers() {
		return messageReceivers;
	}

	@Override
	public MessageReceiverCache getMessageReceiverCache(Class<?> type) {
		return messageReceivers.getSelectionCache(type);
	}

	@Override
	public CommandManager commandManager() {
		return commandManager;
	}

	@Override
	public void addActiveCommand(ObservableCommand command) {
		((GuiceCommandManager) commandManager).addActiveCommand(command);
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	private final String name;
	private final String uuid;
	private final boolean inherited;
	private final DefaultMessageReceiverRegistry messageReceivers;

	private final CommandManager commandManager;

}
