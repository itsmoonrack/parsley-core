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
	private final SwingCommand worker;

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	public SwingCommandAdapter(Object target, Method execute) {
		this.target = target;
		this.executeMethod = execute;
		this.worker = new SwingCommand();
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

	}

	@Override
	protected void doResume() {

	}

	@Override
	protected void doCancel() {
		worker.cancel(true);
	}

	@Override
	protected void doExecute() {
		worker.execute();
	}

	private class SwingCommand extends SwingWorker<Object, Object> {

		@Override
		protected Object doInBackground() throws Exception {
			lifecycle.beforeExecution(target, data);
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
				lifecycle.afterCompletion(target, result);
				// TODO: Use this result.
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

	}

}
