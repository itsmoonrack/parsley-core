package org.spicefactory.parsley.command.impl;

/**
 * Strategy that allows to determine the trigger message type for a command based on the type of the command.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface CommandTriggerProvider {

	/**
	 * Returns the trigger message for the specified command type or null if it cannot be determined.
	 * @param commandType the type of the target command
	 * @return the trigger message type for the specified command
	 */
	Class<?> getTriggerType(Class<?> commandType);

}
