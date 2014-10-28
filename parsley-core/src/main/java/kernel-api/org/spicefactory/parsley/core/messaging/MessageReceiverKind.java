package org.spicefactory.parsley.core.messaging;

import org.spicefactory.parsley.core.command.CommandStatus;

/**
 * Enumeration for the different kinds of message receivers.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public enum MessageReceiverKind {

	/**
	 * Constant for a regular message target (like MessageHandler or Command).
	 */
	TARGET,

	/**
	 * Constant for an error handler.
	 */
	ERROR_HANDLER,

	/**
	 * Constant for a command observer for the status EXECUTE, matching by trigger message and selector.
	 */
	COMMAND_EXECUTE_BY_TRIGGER,

	/**
	 * Constant for a command observer for the status COMPLETE, matching by trigger message and selector.
	 */
	COMMAND_COMPLETE_BY_TRIGGER,

	/**
	 * Constant for a command observer for the status ERROR, matching by trigger message and selector.
	 */
	COMMAND_ERROR_BY_TRIGGER,

	/**
	 * Constant for a command observer for the status CANCEL, matching by trigger message and selector.
	 */
	COMMAND_CANCEL_BY_TRIGGER,

	/**
	 * Constant for a command observer for the status EXECUTE, matching by command type and id.
	 */
	COMMAND_EXECUTE_BY_TYPE,

	/**
	 * Constant for a command observer for the status COMPLETE, matching by command type and id.
	 */
	COMMAND_COMPLETE_BY_TYPE,

	/**
	 * Constant for a command observer for the status ERROR, matching by command type and id.
	 */
	COMMAND_ERROR_BY_TYPE,

	/**
	 * Constant for a command observer for the status CANCEL, matching by command type and id.
	 */
	COMMAND_CANCEL_BY_TYPE;

	public static MessageReceiverKind forCommandStatus(CommandStatus status, boolean byTrigger) {
		switch (status) {
			case EXECUTE:
				return byTrigger ? COMMAND_EXECUTE_BY_TRIGGER : COMMAND_EXECUTE_BY_TYPE;
			case COMPLETE:
				return byTrigger ? COMMAND_COMPLETE_BY_TRIGGER : COMMAND_COMPLETE_BY_TYPE;
			case CANCEL:
				return byTrigger ? COMMAND_CANCEL_BY_TRIGGER : COMMAND_CANCEL_BY_TYPE;
			case ERROR:
				return byTrigger ? COMMAND_ERROR_BY_TRIGGER : COMMAND_ERROR_BY_TYPE;
			default:
				return null;
		}
	}

}
