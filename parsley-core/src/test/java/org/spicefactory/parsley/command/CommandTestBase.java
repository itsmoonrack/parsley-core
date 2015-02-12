package org.spicefactory.parsley.command;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.emptyCollectionOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.spicefactory.lib.command.data.CommandData;
import org.spicefactory.parsley.command.observer.CommandObservers;
import org.spicefactory.parsley.command.observer.CommandStatusFlags;
import org.spicefactory.parsley.command.target.CommandBase;
import org.spicefactory.parsley.command.trigger.Trigger;
import org.spicefactory.parsley.command.trigger.TriggerA;
import org.spicefactory.parsley.command.trigger.TriggerB;
import org.spicefactory.parsley.core.command.CommandManager;
import org.spicefactory.parsley.core.context.Context;
import org.spicefactory.parsley.core.scope.Scope;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public abstract class CommandTestBase {

	private Context context;
	private CommandManager manager;

	private CommandStatusFlags status;
	private CommandObservers observers;

	private CommandBase lastCommand;

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

		// When
		complete(0);

		// Then
		validateManager(0);
		validateStatus(false);
		validateResults("1", "2");
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

		complete(0);

		validateManager(0);
		validateStatus(false);
		validateResults("1", "2");
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	protected abstract void execute();

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
		List<CommandBase> commands = getActiveCommands(Trigger.class);
		assertThat(commands, hasSize(greaterThanOrEqualTo(index + 1)));
		lastCommand = commands.get(index);
	}

	protected void validateManager(int count) {
		List<CommandBase> commands = getActiveCommands(Trigger.class);
		assertThat(commands, hasSize(count));
		commands = getActiveCommands(TriggerA.class);
		assertThat(commands, hasSize(count));
		commands = getActiveCommands(TriggerB.class);
		assertThat(commands, hasSize(0));
	}

	protected abstract List<CommandBase> getActiveCommands(Class<?> type);

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
		assertThat(removeExecutorResults(observers.results), contains(results));
		assertThat(removeExecutorResults(observers.resultsA), contains(results));
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

	protected abstract void configureSingleCommand();

	protected abstract void configureCommandSequence();

	protected abstract void configureParallelCommands();

	protected abstract void configureCommandFlow();
}
