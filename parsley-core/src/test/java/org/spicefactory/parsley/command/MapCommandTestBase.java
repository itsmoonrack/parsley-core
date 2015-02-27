package org.spicefactory.parsley.command;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.spicefactory.parsley.command.target.CommandBase;
import org.spicefactory.parsley.command.target.SyncCommand;
import org.spicefactory.parsley.command.trigger.TriggerA;
import org.spicefactory.parsley.core.command.ObservableCommand;
import org.spicefactory.parsley.core.scope.Scope;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public abstract class MapCommandTestBase extends CommandTestBase {

	@Test
	public void testNoMatchingTrigger() {
		// Given
		configureSingleCommand();
		validateManager(0);

		// When
		dispatchMessage("foo");

		// Then
		validateManager(0);
	}

	@Test
	public void testNoMatchingScope() {
		// Given
		configureLocalScope();
		validateManager(0);

		// When
		dispatchMessage(new TriggerA(), Scope.GLOBAL);

		// Then
		validateManager(0);
	}

	@Test
	public void testNoMatchingSelector() {
		// Given
		configureSelector();
		validateManager(0);

		// When
		dispatchMessage(new TriggerA(), null, "no-match");

		// Then
		validateManager(0);
	}

	@Test
	public void testLocalScope() {
		// Given
		configureLocalScope();
		validateManager(0);

		// When
		dispatchMessage(new TriggerA(), Scope.LOCAL);

		// Then
		validateExecution();
	}

	@Test
	public void testOrder() {
		// Given
		SyncCommand.instances = new ArrayList<SyncCommand>();
		configureOrder();
		validateManager(0);

		// When
		execute();

		// Then
		validateManager(0);
		validateStatus(false);
		validateResults("1", "2");
		validateSyncLifecycle(0);
		validateSyncLifecycle(1);
	}

	private void validateSyncLifecycle(int index) {
		assertThat(SyncCommand.instances.size(), greaterThanOrEqualTo(index + 1));
		//		assertThat(SyncCommand.instances.get(index).destroyCount, equalTo(1));
	}

	@Test
	public void testSelector() {
		// Given
		configureSelector();
		validateManager(0);

		// When
		dispatchMessage(new TriggerA(), null, "selector");

		// Then
		validateExecution();
	}

	protected abstract void configureLocalScope();

	protected abstract void configureOrder();

	protected abstract void configureSelector();

	@Override
	protected List<ObservableCommand> getActiveCommands(Class<?> type) {
		List<ObservableCommand> commands = manager.getActiveCommandsByTrigger(type);
		List<ObservableCommand> result = new ArrayList<ObservableCommand>();
		for (ObservableCommand com : commands) {
			if (com.getCommand() instanceof CommandBase)
				result.add(com);
		}
		return result;
	}

	@Override
	protected void execute() {
		dispatchMessage(new TriggerA());
	}

	private void validateExecution() {
		validateManager(1);
		validateStatus(true);
		validateResults();

		complete(0, true);

		validateManager(0);
		validateStatus(false);
		validateResults(true);
		validateLifecycle();
	}

}
