package org.spicefactory.parsley.command.swing;

import java.lang.reflect.Method;

import javax.swing.SwingWorker;

import org.spicefactory.lib.command.CommandResult;
import org.spicefactory.lib.command.adapter.CommandAdapter;
import org.spicefactory.lib.command.base.AbstractSuspendableCommand;
import org.spicefactory.lib.command.base.DefaultCommandResult;
import org.spicefactory.lib.command.data.CommandData;
import org.spicefactory.lib.command.data.DefaultCommandData;
import org.spicefactory.lib.command.lifecycle.CommandLifecycle;

class SwingCommandAdapter extends AbstractSuspendableCommand implements CommandAdapter {

	private CommandLifecycle lifecycle;
	private DefaultCommandData data = new DefaultCommandData();

	private final Object target;
	private final Method executeMethod;
	private final Method cancelMethod;
	private final Method resultMethod;
	private final Method errorMethod;
	private final boolean async;
	private final SwingCommand worker;

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	public SwingCommandAdapter(Object target, Method execute, Method cancel, Method result, Method error, boolean async) {
		this.target = target;
		this.executeMethod = execute;
		this.cancelMethod = cancel;
		this.resultMethod = result;
		this.errorMethod = error;
		this.async = async;
		this.worker = async ? new SwingCommand() : null;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	@Override
	public void prepare(CommandLifecycle lifecycle, CommandData data) {
		this.lifecycle = lifecycle;
		this.data = new DefaultCommandData(data);
	}

	@Override
	public boolean isCancellable() {
		return true;
	}

	@Override
	public boolean isSuspendable() {
		return false;
	}

	@Override
	public Object getTarget() {
		return target;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	@Override
	protected void doSuspend() {
		throw new IllegalAccessError("Command is not suspendable.");
	}

	@Override
	protected void doResume() {
		throw new IllegalAccessError("Command cannot be resumed.");
	}

	@Override
	protected void doCancel() {
		worker.cancel(true);
	}

	@Override
	protected void doExecute() {
		lifecycle.beforeExecution(target, data);
		try {
			if (async) {
				worker.execute();
			} else {
				// Result can be null if invoked method return type is void.
				Object result = executeMethod.invoke(target, getParameters());
				handleResult(result);
			}
		}
		catch (Exception e) {
			afterCompletion(DefaultCommandResult.forException(target, e));
			exception(e);
		}
	}

	private Object[] getParameters() {
		Class<?>[] parameterTypes = executeMethod.getParameterTypes();
		Object[] parameters = new Object[parameterTypes.length];
		for (int i = 0; i < parameterTypes.length; ++i) {
			parameters[i] = data.getObject(parameterTypes[i]);
		}
		return parameters;
	}

	private void afterCompletion(CommandResult result) {
		lifecycle.afterCompletion(target, result);
	}

	private void handleResult(Object result) {

	}

	private class SwingCommand extends SwingWorker<Object, Object> {

		@Override
		protected Object doInBackground() throws Exception {
			return executeMethod.invoke(target, getParameters());
		}

		@Override
		protected void done() {
			// TODO: This need to be worked when I'll have time to do it.
			// For now it works but is not robust (exceptions, etc. are not handled properly).
			CommandResult result = null;
			try {
				if (isCancelled()) {
					result = DefaultCommandResult.forCancellation(target);
				} else {
					result = DefaultCommandResult.forCompletion(target, get());
				}
			}
			catch (Exception e) {
				logger.debug("An exception has been raised in command.", e.getCause());
				result = DefaultCommandResult.forException(target, e.getCause());
			}
			finally {
				afterCompletion(result);
				// TODO: Use this result.
			}
		}

	}

}
