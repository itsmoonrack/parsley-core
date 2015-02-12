package org.spicefactory.parsley.command.swing;

import java.lang.reflect.Method;

import javax.swing.SwingWorker;

import org.spicefactory.lib.command.CommandResult;
import org.spicefactory.lib.command.adapter.CommandAdapter;
import org.spicefactory.lib.command.base.AbstractSuspendableCommand;
import org.spicefactory.lib.command.base.DefaultCommandResult;
import org.spicefactory.lib.command.builder.CommandProxyBuilder;
import org.spicefactory.lib.command.data.CommandData;
import org.spicefactory.lib.command.data.DefaultCommandData;
import org.spicefactory.lib.command.events.CommandEvent;
import org.spicefactory.lib.command.events.CommandException;
import org.spicefactory.lib.command.lifecycle.CommandLifecycle;
import org.spicefactory.lib.command.proxy.CommandProxy;
import org.spicefactory.lib.command.result.ResultProcessors;
import org.spicefactory.lib.event.EventListener;

class SwingCommandAdapter extends AbstractSuspendableCommand implements CommandAdapter {

	private CommandLifecycle lifecycle;
	private CommandProxy resultProcessor;
	private DefaultCommandData data = new DefaultCommandData();

	private final Object target;
	private final Method executeMethod;
	private final Method cancelMethod;
	private final Method resultMethod;
	private final Method exceptionMethod;
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
		this.exceptionMethod = error;
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
		return async;
	}

	@Override
	public boolean isSuspendable() {
		return false;
	}

	@Override
	public Object getTarget() {
		return target;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + target + "]";
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
				System.err.println("invoke(target) > " + result);
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
		CommandProxyBuilder builder = result == null ? null : ResultProcessors.newProcessor(target, result);
		if (builder != null) {
			processResult(builder);
		} else {
			handleCompletion(result);
		}
	}

	private void handleCompletion(Object result) {
		result = invokeResultHandler(resultMethod, result);
		if (result instanceof Throwable) {
			handleException((Throwable) result);
			return;
		}
		afterCompletion(DefaultCommandResult.forCompletion(target, result));
		resultProcessor = null;
		complete(result);
	}

	private void handleException(Throwable cause) {
		cause = (Throwable) invokeResultHandler(exceptionMethod, cause);
		afterCompletion(DefaultCommandResult.forException(target, cause));
		resultProcessor = null;
		exception(cause);
	}

	private Object invokeResultHandler(Method method, Object value) {
		if (method == null)
			return value;

		Object param = getParam(method, value);
		try {
			if (method.getReturnType().isAssignableFrom(Void.class)) {
				method.invoke(target, param);
				return value;
			} else {
				return method.invoke(target, param);
			}
		}
		catch (Throwable t) {
			return t;
		}
	}

	private Object getParam(Method method, Object value) {
		if (value instanceof CommandException) {
			if (!(method.getParameterTypes()[0].isAssignableFrom(CommandException.class))) {
				return ((CommandException) value).getCause();
			}
		}
		return value;
	}

	private void processResult(CommandProxyBuilder builder) {
		resultProcessor = builder //
				.result(new CommandCompletionCallback()) //
				.exception(new CommandExceptionCallback()) //
				.cancel(new CommandCancellationCallback()) //
				.execute();
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
			handleResult(result);
		}

	}

	/////////////////////////////////////////////////////////////////////////////
	// Pre-Java 8 implementation.
	/////////////////////////////////////////////////////////////////////////////

	private class CommandCompletionCallback implements EventListener<CommandEvent> {

		@Override
		public void process(CommandEvent event) {
			System.err.println("CommandCompletionCallback");
		}

	}

	private class CommandExceptionCallback implements EventListener<CommandEvent> {

		@Override
		public void process(CommandEvent event) {
			System.err.println("CommandExceptionCallback");
		}
	}

	private class CommandCancellationCallback implements EventListener<CommandEvent> {

		@Override
		public void process(CommandEvent event) {
			System.err.println("CommandCancellationCallback");
		}
	}

}
