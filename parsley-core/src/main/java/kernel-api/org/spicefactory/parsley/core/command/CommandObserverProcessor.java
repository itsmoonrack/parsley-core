package org.spicefactory.parsley.core.command;

import org.spicefactory.parsley.core.messaging.MessageProcessor;

/**
 * Responsible for processing command observers. Will be passed to registered CommandObserver instances which may chose to cancel or suspend and
 * later resume the message processing.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface CommandObserverProcessor extends MessageProcessor {

	/**
	 * The actual command instance.
	 */
	Object getCommand();

	/**
	 * The result produced by the command.
	 */
	Object getResult();

	/**
	 * The status of the command.
	 */
	CommandStatus getCommandStatus();

	/**
	 * Indicates whether this processor handles a root command or a nested command.
	 * <p>
	 * This property is true if the command is a simple stand-alone command or the root command of a sequence or flow. It is flow if it is a
	 * command nested in a sequence or flow.
	 */
	boolean isRoot();

	/**
	 * Changes the result that the command produced.
	 * <p>
	 * Subsequent command observers (if any) will receive the new result.
	 * <p>
	 * Observers that had already been processed will not get invoked a second time.
	 * <p>
	 * @param result the new result to pass to subsequent command observers
	 * @param error true if the result represents an error
	 */
	void changeResult(Object result, boolean error);

}
