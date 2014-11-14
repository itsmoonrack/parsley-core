package org.spicefactory.parsley.command;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.spicefactory.lib.command.CommandResult;
import org.spicefactory.lib.command.data.CommandData;
import org.spicefactory.lib.command.lifecycle.CommandLifecycle;
import org.spicefactory.lib.command.proxy.CommandProxy;
import org.spicefactory.parsley.core.command.CommandStatus;
import org.spicefactory.parsley.core.command.ManagedCommandProxy;
import org.spicefactory.parsley.core.command.ObservableCommand;
import org.spicefactory.parsley.core.context.Context;
import org.spicefactory.parsley.core.messaging.Message;

class ManagedCommandLifecycle implements CommandLifecycle {

	private final Context context;
	private final Message trigger;
	private final Map<Object, DefaultObservableCommand> observables;

	private boolean root = true;
	private String nextId;

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	ManagedCommandLifecycle(Context context, ManagedCommandProxy root) {
		this(context, root, null);
	}

	ManagedCommandLifecycle(Context context, ManagedCommandProxy root, @Nullable Message trigger) {
		this.context = context;
		this.trigger = trigger;
		this.nextId = root.getID();
		this.observables = new HashMap<Object, DefaultObservableCommand>();
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	@Override
	public <T> T createInstance(Class<T> type, CommandData data) {
		return context.getInstance(type); // Data on constructor is not supported for commands.
	}

	@Override
	public void beforeExecution(Object command, CommandData data) {
		if (isObservableTarget(command)) {
			context.getScopeManager().observeCommand(createObservableCommand(command));
		} else if (command instanceof ManagedCommandProxy) {
			nextId = ((ManagedCommandProxy) command).getID();
		}
	}

	@Override
	public void afterCompletion(Object command, CommandResult result) {
		if (observables.containsKey(command)) {
			observables.get(command).setResult(result);
			observables.remove(command); // TODO: Check if this need to be removed actually...
		}
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	private boolean isObservableTarget(Object command) {
		return !(command instanceof CommandProxy);
	}

	private ObservableCommand createObservableCommand(Object command) {
		DefaultObservableCommand observable = new DefaultObservableCommand(command, command.getClass(), nextId, root);
		root = false;
		nextId = null;
		observables.put(command, observable);
		return observable;
	}

	private class DefaultObservableCommand implements ObservableCommand {

		private final Object command;
		private final Class<?> type;
		private final boolean root;
		private final String id;
		private final List<CommandObserver> callbacks;

		private Object result;
		private CommandStatus status;

		public DefaultObservableCommand(Object command, Class<?> type, String id, boolean root) {
			this.command = command;
			this.type = type;
			this.root = root;
			this.id = id;
			this.status = CommandStatus.EXECUTE;
			this.callbacks = new LinkedList<CommandObserver>();
		}

		@Override
		public Message trigger() {
			return trigger;
		}

		@Override
		public Object command() {
			return command;
		}

		@Override
		public String id() {
			return id;
		}

		@Override
		public Class<?> type() {
			return type;
		}

		@Override
		public Object result() {
			return result;
		}

		public void setResult(CommandResult result) {
			this.status = (result.complete() ? CommandStatus.COMPLETE : (result.value() == null ? CommandStatus.CANCEL : CommandStatus.ERROR));
			this.result = result.value();
			for (CommandObserver callback : callbacks) {
				callback.update(this);
			}
			callbacks.clear();
		}

		@Override
		public CommandStatus status() {
			return status;
		}

		@Override
		public boolean root() {
			return root;
		}

		@Override
		public void observe(CommandObserver callback) {
			callbacks.add(callback);
		}

	}
}
