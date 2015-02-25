package org.spicefactory.parsley.core.command;

import javax.annotation.Nullable;

import org.spicefactory.parsley.core.messaging.Message;

/**
 * Represents a single command and holds all the information needed for command observers to process this command and its result.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface ObservableCommand {

	/**
	 * The message that triggered the command. This property is null when the command was started programmatically.
	 */
	@Nullable
	Message<Object> getTrigger();

	/**
	 * The actual command instance.
	 */
	Object getCommand();

	/**
	 * The id the command is registered with in the Context.
	 */
	int getId();

	/**
	 * The type of the command.
	 */
	Class<?> getType();

	/**
	 * The result produced by the command.
	 */
	Object getResult();

	/**
	 * The status of the command.
	 */
	CommandStatus getStatus();

	/**
	 * Indicates whether this command is a root command or a nested command. This property is true if the command is a simple standalone command
	 * or the root command of a sequence or flow. It is flow if it is a command nested in a sequence or flow.
	 */
	boolean isRoot();

	/**
	 * Observes the completion of this command, no matter whether it successfully completes, aborts with an error or gets cancelled. The callback
	 * function must accept an argument of type ObservableCommand.
	 * @param callback the callback to invoke when the command completes
	 */
	void observe(CommandObserver callback);

	/**
	 * Observes the completion of a command.
	 */
	public interface CommandObserver {

		/**
		 * This method is called whenever the observed command has changed.
		 */
		void handleCommand(ObservableCommand command);

	}

}
