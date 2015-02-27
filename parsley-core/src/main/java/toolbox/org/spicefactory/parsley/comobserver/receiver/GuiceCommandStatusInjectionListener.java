package org.spicefactory.parsley.comobserver.receiver;

import java.lang.reflect.Method;

import org.spicefactory.parsley.comobserver.annotation.CommandStatus;
import org.spicefactory.parsley.core.command.CommandManager;
import org.spicefactory.parsley.core.context.provider.ObjectProvider;
import org.spicefactory.parsley.core.messaging.MessageReceiverKind;
import org.spicefactory.parsley.core.scope.Scope;
import org.spicefactory.parsley.core.scope.ScopeManager;
import org.spicefactory.parsley.messaging.receiver.MessageReceiverInfo;

import com.google.inject.Provider;
import com.google.inject.spi.InjectionListener;

public class GuiceCommandStatusInjectionListener implements InjectionListener<Object> {

	private final Method method;
	private final CommandStatus methodInfo;
	private final Provider<ScopeManager> scopeManager;
	private final Provider<CommandManager> commandManager;

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	public GuiceCommandStatusInjectionListener(Provider<ScopeManager> scopeManager, Provider<CommandManager> commandManager, Method method) {
		this.method = method;
		this.methodInfo = method.getAnnotation(CommandStatus.class);
		this.scopeManager = scopeManager;
		this.commandManager = commandManager;
	}

	@Override
	public void afterInjection(Object injectee) {
		final ObjectProvider provider = new ObjectProvider(injectee);
		final MessageReceiverInfo info = new MessageReceiverInfo(methodInfo.type(), methodInfo.selector(), Integer.MIN_VALUE);
		final CommandStatusFlag execute = new CommandStatusFlag(info, MessageReceiverKind.COMMAND_EXECUTE_BY_TRIGGER, commandManager.get());
		final CommandStatusFlag complete = new CommandStatusFlag(info, MessageReceiverKind.COMMAND_COMPLETE_BY_TRIGGER, commandManager.get());
		final CommandStatusFlag exception = new CommandStatusFlag(info, MessageReceiverKind.COMMAND_EXCEPTION_BY_TRIGGER, commandManager.get());
		final CommandStatusFlag cancel = new CommandStatusFlag(info, MessageReceiverKind.COMMAND_CANCEL_BY_TRIGGER, commandManager.get());

		execute.init(provider, method);
		complete.init(provider, method);
		exception.init(provider, method);
		cancel.init(provider, method);

		Scope scope = scopeManager.get().getScope(methodInfo.scope());
		scope.getMessageReceivers().addCommandObserver(execute);
		scope.getMessageReceivers().addCommandObserver(complete);
		scope.getMessageReceivers().addCommandObserver(exception);
		scope.getMessageReceivers().addCommandObserver(cancel);
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

}
