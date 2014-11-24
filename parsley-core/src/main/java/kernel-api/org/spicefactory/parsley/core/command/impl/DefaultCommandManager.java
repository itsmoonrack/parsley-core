package org.spicefactory.parsley.core.command.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.spicefactory.parsley.core.command.CommandManager;
import org.spicefactory.parsley.core.command.ObservableCommand;
import org.spicefactory.parsley.core.command.ObservableCommand.CommandObserver;
import org.spicefactory.parsley.core.messaging.Selector;

/**
 * Default implementation of the CommandManager interface.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public class DefaultCommandManager implements CommandManager, CommandObserver {

	private final List<ObservableCommand> commands = new ArrayList<ObservableCommand>();

	/////////////////////////////////////////////////////////////////////////////
	// Framework-private.
	/////////////////////////////////////////////////////////////////////////////

	/**
	 * Adds an active command to be managed by this instance.
	 * @param command the active command to add
	 */
	public void addActiveCommand(ObservableCommand command) {
		commands.add(command);
		command.observe(this);
	}

	@Override
	public void update(ObservableCommand command) {
		commands.remove(command);
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	@Override
	public boolean hasActiveCommandsForTrigger(Class<?> messageType, String selector) {
		for (ObservableCommand command : commands) {
			if (matchesByTrigger(command, messageType, selector)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasActiveCommandsOfType(Class<?> commandType, String name) {
		for (ObservableCommand command : commands) {
			if (matchesByType(command, commandType, name)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<ObservableCommand> getActiveCommandsByTrigger(Class<?> messageType, String selector) {
		List<ObservableCommand> result = new ArrayList<ObservableCommand>();
		for (ObservableCommand command : commands) {
			if (matchesByTrigger(command, messageType, selector)) {
				result.add(command);
			}
		}
		return result;
	}

	@Override
	public List<ObservableCommand> getActiveCommandsByType(Class<?> commandType, String name) {
		List<ObservableCommand> result = new ArrayList<ObservableCommand>();
		for (ObservableCommand command : commands) {
			if (matchesByType(command, commandType, name)) {
				result.add(command);
			}
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	private boolean matchesByTrigger(ObservableCommand command, Class<?> messageType, @Nullable String selector) {
		return (command.trigger() != null && messageType.isAssignableFrom(command.trigger().getInstance().getClass()) && (selector == Selector.NONE || selector == command
				.trigger().selector()));
	}

	private boolean matchesByType(ObservableCommand command, Class<?> commandType, @Nullable String name) {
		return (commandType.isAssignableFrom(command.command().getClass()) && (name == null || name == command.id()));
	}
}
