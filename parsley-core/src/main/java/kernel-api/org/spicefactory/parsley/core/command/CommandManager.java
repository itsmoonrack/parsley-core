package org.spicefactory.parsley.core.command;

import java.util.List;

import javax.annotation.Nullable;

/**
 * Responsible for managing all active commands for a single scope.
 * <p>
 * Since each scope has its own manager, instances can be obtained through the <code>Scope.commandManager</code> property.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface CommandManager {

	/**
	 * Indicates whether this scope has any active commands triggered by a message that matches the specified type.
	 * @param messageType the type of the message that triggered the commands
	 * @return true if there is at least one matching command in this scope.
	 */
	boolean hasActiveCommandsForTrigger(Class<?> messageType);

	/**
	 * Indicates whether this scope has any active commands triggered by a message that matches the specified type and selector.
	 * @param messageType the type of the message that triggered the commands
	 * @param selector the selector of the message that triggered the commands
	 * @return true if there is at least one matching command in this scope.
	 */
	boolean hasActiveCommandsForTrigger(Class<?> messageType, int selector);

	/**
	 * Indicates whether this scope has any active commands that matches the specified command type.
	 * @param commandType the type of the command
	 * @return true if there is at least one matching command in this scope.
	 */
	boolean hasActiveCommandsOfType(Class<?> commandType);

	/**
	 * Indicates whether this scope has any active commands that matches the specified command type and id.
	 * @param commandType the type of the command
	 * @param id the name the command is registered with in the Context
	 * @return true if there is at least one matching command in this scope.
	 */
	boolean hasActiveCommandsOfType(Class<?> commandType, @Nullable String id);

	/**
	 * Returns all active commands triggered by a message that matches the specified type in this scope.
	 * @param messageType the type of the message that triggered the commands
	 * @return all matching ObservableCommand instances.
	 */
	List<ObservableCommand> getActiveCommandsByTrigger(Class<?> messageType);

	/**
	 * Returns all active commands triggered by a message that matches the specified type and selector in this scope.
	 * @param messageType the type of the message that triggered the commands
	 * @param selector the selector of the message that triggered the commands
	 * @return all matching ObservableCommand instances.
	 */
	List<ObservableCommand> getActiveCommandsByTrigger(Class<?> messageType, int selector);

	/**
	 * Returns all active commands of the specified type.
	 * <p>
	 * If an id is specified it refers to the id the command is registered with in the Context.
	 * @param commandType the type of the command
	 * @return all matching ObservableCommand instances
	 */
	List<ObservableCommand> getActiveCommandsByType(Class<?> commandType);

	/**
	 * Returns all active commands of the specified type.
	 * <p>
	 * If an id is specified it refers to the id the command is registered with in the Context.
	 * @param commandType the type of the command
	 * @param name the name the command is registered with in the Context
	 * @return all matching ObservableCommand instances
	 */
	List<ObservableCommand> getActiveCommandsByType(Class<?> commandType, @Nullable String id);

}
