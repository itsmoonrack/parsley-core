package org.spicefactory.parsley.command.impl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.spicefactory.lib.command.CommandResult;
import org.spicefactory.lib.command.data.CommandData;
import org.spicefactory.lib.command.lifecycle.CommandLifecycle;
import org.spicefactory.lib.command.proxy.CommandProxy;
import org.spicefactory.parsley.core.command.CommandStatus;
import org.spicefactory.parsley.core.command.ManagedCommandProxy;
import org.spicefactory.parsley.core.command.ObservableCommand;
import org.spicefactory.parsley.core.messaging.Message;
import org.spicefactory.parsley.core.scope.ScopeManager;

import com.google.inject.Injector;

/**
 * Extension of the stand-alone CommandLifecycle from library that deals with retrieving commands from Context for the execution time and passing
 * them to the CommandManager.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public class GuiceManagedCommandLifecycle implements CommandLifecycle {

	@Inject
	private Injector injector;
	private boolean root = true;
	private String nextId;

	private final Message trigger;
	private final ScopeManager scopeManager;
	private final Map<Object, GuiceObservableCommand> observables;

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	@Inject
	GuiceManagedCommandLifecycle(ScopeManager scopeManager, ManagedCommandProxy root) {
		this(scopeManager, root, null);
	}

	GuiceManagedCommandLifecycle(ScopeManager scopeManager, ManagedCommandProxy root, Message trigger) {
		this.nextId = root.getID();
		this.trigger = trigger;
		this.scopeManager = scopeManager;
		this.observables = new HashMap<Object, GuiceObservableCommand>();
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	@Override
	public <T> T createInstance(Class<T> type, CommandData data) {
		return injector.getInstance(type);
	}

	@Override
	public void beforeExecution(Object command, CommandData data) {
		if (isObservableTarget(command)) {
			scopeManager.observeCommand(createObservableCommand(command));
		} else if (command instanceof CommandProxy) {
			// TODO: GuiceCommandProxy
		}
	}

	@Override
	public void afterCompletion(Object command, CommandResult result) {
		if (observables.containsKey(command)) {
			observables.get(command).setResult(result);
			//observables.remove(command); // TODO: Check if this need to be removed actually...
		}
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	private boolean isObservableTarget(Object command) {
		return !(command instanceof CommandProxy);
	}

	private ObservableCommand createObservableCommand(Object command) {
		GuiceObservableCommand observable = new GuiceObservableCommand(command, command.getClass(), nextId, root);
		root = false;
		nextId = null;
		observables.put(command, observable);
		return observable;
	}

	private class GuiceObservableCommand implements ObservableCommand {

		private final Object command;
		private final Class<?> type;
		private final boolean root;
		private final String id;
		private final List<CommandObserver> callbacks;

		private Object result;
		private CommandStatus status;

		public GuiceObservableCommand(Object command, Class<?> type, String id, boolean root) {
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
