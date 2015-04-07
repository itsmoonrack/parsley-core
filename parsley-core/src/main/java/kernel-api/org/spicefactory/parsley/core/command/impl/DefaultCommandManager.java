package org.spicefactory.parsley.core.command.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.inject.Singleton;

import org.spicefactory.parsley.core.command.CommandManager;
import org.spicefactory.parsley.core.command.ObservableCommand;
import org.spicefactory.parsley.core.command.ObservableCommand.CommandObserver;
import org.spicefactory.parsley.core.messaging.Message;
import org.spicefactory.parsley.core.messaging.Selector;

/**
 * Default implementation of the CommandManager interface.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
@Singleton
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
	public void handleCommand(ObservableCommand command) {
		commands.remove(command);
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	@Override
	public boolean hasActiveCommandsForTrigger(Class<?> messageType) {
		return hasActiveCommandsForTrigger(messageType, Selector.NONE);
	}

	@Override
	public boolean hasActiveCommandsForTrigger(Class<?> messageType, Object selector) {
		for (ObservableCommand command : commands) {
			if (matchesByTrigger(command, messageType, selector)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean hasActiveCommandsOfType(Class<?> commandType) {
		return hasActiveCommandsOfType(commandType, null);
	}

	@Override
	public boolean hasActiveCommandsOfType(Class<?> commandType, String id) {
		for (ObservableCommand command : commands) {
			if (matchesByType(command, commandType, id)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<ObservableCommand> getActiveCommandsByTrigger(Class<?> messageType) {
		return getActiveCommandsByTrigger(messageType, Selector.NONE);
	}

	@Override
	public List<ObservableCommand> getActiveCommandsByTrigger(Class<?> messageType, Object selector) {
		List<ObservableCommand> result = new ArrayList<ObservableCommand>();
		for (ObservableCommand command : commands) {
			if (matchesByTrigger(command, messageType, selector)) {
				result.add(command);
			}
		}
		return result;
	}

	@Override
	public List<ObservableCommand> getActiveCommandsByType(Class<?> commandType) {
		return getActiveCommandsByType(commandType, null);
	}

	@Override
	public List<ObservableCommand> getActiveCommandsByType(Class<?> commandType, String id) {
		List<ObservableCommand> result = new ArrayList<ObservableCommand>();
		for (ObservableCommand command : commands) {
			if (matchesByType(command, commandType, id)) {
				result.add(command);
			}
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	private boolean matchesByTrigger(ObservableCommand command, Class<?> messageType, Object selector) {
		if (command.getTrigger() == null) {
			return false;
		}
		Message<Object> trigger = command.getTrigger();
		return messageType.isAssignableFrom(trigger.getInstance().getClass())
				&& (selector.equals(Selector.NONE) || selector.equals(trigger.getSelector()));
	}

	private boolean matchesByType(ObservableCommand command, Class<?> commandType, @Nullable String id) {
		return commandType.isAssignableFrom(command.getCommand().getClass()) && (id == null || id.equals(command.getId()));
	}
}
