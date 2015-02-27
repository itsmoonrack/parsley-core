package org.spicefactory.parsley.command;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.junit.Test;
import org.spicefactory.lib.command.data.CommandData;
import org.spicefactory.lib.command.events.CommandException;
import org.spicefactory.parsley.command.observer.CommandObservers;
import org.spicefactory.parsley.command.observer.CommandStatusFlags;
import org.spicefactory.parsley.command.target.CommandBase;
import org.spicefactory.parsley.command.trigger.Trigger;
import org.spicefactory.parsley.command.trigger.TriggerA;
import org.spicefactory.parsley.command.trigger.TriggerB;
import org.spicefactory.parsley.core.command.CommandManager;
import org.spicefactory.parsley.core.command.ObservableCommand;
import org.spicefactory.parsley.core.context.Context;
import org.spicefactory.parsley.core.messaging.Selector;
import org.spicefactory.parsley.core.scope.Scope;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public abstract class CommandTestBase {

	protected Context context;
	protected CommandManager manager;

	protected CommandStatusFlags status;
	protected CommandObservers observers;

	protected CommandBase lastCommand;

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	protected void setContext(Context value) {
		context = value;
		manager = context.getScopeManager().getScope(Scope.GLOBAL).getCommandManager();
		status = context.getInstance(CommandStatusFlags.class);
		observers = context.getInstance(CommandObservers.class);
	}

	protected CommandManager getManager() {
		return manager;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public Tests.
	/////////////////////////////////////////////////////////////////////////////

	@Test
	public void testSingleCommand() {
		// Given
		if (context == null) {
			configureSingleCommand();
		} else {
			observers.reset();
		}

		validateManager(0);

		// When
		execute();

		// Then
		validateManager(1);
		validateStatus(true);
		validateResults();

		// When
		complete(0, true);

		// Then
		validateManager(0);
		validateStatus(false);
		validateResults(true);
		validateLifecycle();
	}

	@Test
	public void testCommandSequence() {
		// Given
		if (context == null) {
			configureCommandSequence();
		} else {
			observers.reset();
		}

		validateManager(0);

		// When
		execute();

		// Then
		validateManager(1);
		validateStatus(true);
		validateResults();

		// When
		complete(0);

		// Then
		validateManager(1);
		validateStatus(true);
		validateResults("1");
		validateLifecycle();

		// When
		complete(0);

		// Then
		validateManager(0);
		validateStatus(false);
		validateResults("1", "2");
		validateLifecycle();
	}

	@Test
	public void testParallelCommands() {
		if (context == null) {
			configureParallelCommands();
		} else {
			observers.reset();
		}

		validateManager(0);

		execute();

		validateManager(2);
		validateStatus(true);
		validateResults();

		complete(0);

		validateManager(1);
		validateStatus(true);
		validateResults("1");
		validateLifecycle();

		complete(0);

		validateManager(0);
		validateStatus(false);
		validateResults("1", "2");
		validateLifecycle();
	}

	// TODO: testCommandFlow()

	@Test
	public void testCancelSequence() {
		// Given
		configureCommandSequence();
		validateManager(0);

		// When
		execute();

		// Then
		validateManager(1);
		validateStatus(true);
		validateResults();

		// When
		complete(0);

		// Then
		validateManager(1);
		validateStatus(true);
		validateResults("1");
		validateLifecycle();

		// When
		cancel(0);

		// Then
		validateManager(0);
		validateStatus(false);
		validateResults("1");
		validateLifecycle();
	}

	@Test
	public void testExceptionInSequence() {
		// Given
		configureCommandSequence();
		validateManager(0);

		// When
		execute();

		// Then
		validateManager(1);
		validateStatus(true);
		validateResults();

		// When
		complete(0);

		// Then
		validateManager(1);
		validateStatus(true);
		validateResults("1");
		validateLifecycle();

		// When
		Object e = new IllegalStateException();
		complete(0, e);

		// Then
		validateManager(0);
		validateStatus(false);
		validateResults("1");
		validateException(e);
		validateLifecycle();
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	protected abstract void execute();

	protected void dispatchMessage(Object message) {
		dispatchMessage(message, Scope.GLOBAL, Selector.NONE);
	}

	protected void dispatchMessage(Object message, @Nullable String scope) {
		dispatchMessage(message, scope, Selector.NONE);
	}

	protected void dispatchMessage(Object message, @Nullable String scope, Object selector) {
		if (scope == null) {
			context.getScopeManager().dispatchMessage(message, selector);
		} else {
			context.getScopeManager().getScope(scope).dispatchMessage(message, selector);
		}
	}

	protected void complete(int index) {
		complete(index, null);
	}

	protected void complete(int index, Object result) {
		setLastCommand(index);
		if (result != null)
			lastCommand.result = result;
		lastCommand.invokeCallback();
	}

	protected void cancel(int index) {
		cancel(index, null);
	}

	protected void cancel(int index, Object result) {
		setLastCommand(index);
		lastCommand.cancel();
	}

	protected void setLastCommand(int index) {
		List<ObservableCommand> commands = getActiveCommands(Trigger.class);
		assertThat(commands, hasSize(greaterThanOrEqualTo(index + 1)));
		lastCommand = (CommandBase) commands.get(index).getCommand();
	}

	protected void validateManager(int count) {
		List<ObservableCommand> commands = getActiveCommands(Trigger.class);
		assertThat(commands, hasSize(count));
		commands = getActiveCommands(TriggerA.class);
		assertThat(commands, hasSize(count));
		commands = getActiveCommands(TriggerB.class);
		assertThat(commands, hasSize(0));
	}

	protected abstract List<ObservableCommand> getActiveCommands(Class<?> type);

	protected void validateStatus(boolean active) {
		validateStatus(active, null);
	}

	protected void validateStatus(boolean active, Object result) {
		validateStatus(active, null, null);
	}

	protected void validateStatus(boolean active, Object result, Object exception) {
		assertThat(status.getTrigger(), equalTo(active));
		assertThat(status.getTriggerA(), equalTo(active));
		assertThat(status.getTriggerB(), equalTo(false));
	}

	protected void validateResults(Object... results) {
		if (results.length > 0) {
			assertThat(removeExecutorResults(observers.results), contains(results));
			assertThat(removeExecutorResults(observers.resultsA), contains(results));
		} else {
			assertThat(removeExecutorResults(observers.results), emptyCollectionOf(Object.class));
			assertThat(removeExecutorResults(observers.resultsA), emptyCollectionOf(Object.class));
		}
		assertThat(removeExecutorResults(observers.resultsB), emptyCollectionOf(Object.class));
	}

	private List<Object> removeExecutorResults(List<Object> results) {
		List<Object> filtered = new ArrayList<Object>();
		for (Object result : results) {
			if (result instanceof CommandData)
				continue;
			filtered.add(result);
		}
		return filtered;
	}

	protected void validateException() {
		validateException(null);
	}

	protected void validateException(Object exception) {
		if (exception == null) {
			assertThat(observers.exceptions, hasSize(0));
		} else {
			assertThat(observers.exceptions, hasSize(1));
			assertThat(rootCause(observers.exceptions.get(0)), equalTo(exception));
		}
	}

	private Object rootCause(Object exception) {
		if (exception instanceof CommandException) {
			return rootCause(((CommandException) exception).getCause());
		} else {
			return exception;
		}
	}

	protected void validateLifecycle() {
		validateLifecycle(1);
	}

	protected void validateLifecycle(int destroyCount) {
		// TODO
		//		fail("Destroy not implemented");
	}

	protected abstract void configureSingleCommand();

	protected abstract void configureCommandSequence();

	protected abstract void configureParallelCommands();

	protected abstract void configureCommandFlow();
}
