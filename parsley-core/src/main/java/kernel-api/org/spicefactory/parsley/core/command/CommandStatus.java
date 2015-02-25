package org.spicefactory.parsley.core.command;

/**
 * Enumeration for the current status of a command.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public enum CommandStatus {

	/**
	 * The status for an active command.
	 */
	EXECUTE,

	/**
	 * The status for a command that successfully completed.
	 */
	COMPLETE,

	/**
	 * The status for a command that finished with an exception.
	 */
	EXCEPTION,

	/**
	 * The status for a command that was cancelled.
	 */
	CANCEL;

}
