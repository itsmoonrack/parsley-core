package org.spicefactory.parsley.core.messaging.receiver;

import org.spicefactory.parsley.core.command.CommandObserverProcessor;
import org.spicefactory.parsley.core.messaging.MessageReceiverKind;

/**
 * Represents a message receiver that gets invoked when an asynchronous command starts or finishes its execution.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface CommandObserver extends MessageReceiver {

	/**
	 * The status this observer is interested in.
	 */
	MessageReceiverKind kind();

	/**
	 * Invoked when a matching command starts or finishes its execution.
	 * <p>
	 * The specified processor may be used to control the processing of remaining observers, like canceling or suspending the processor.
	 * </p>
	 * @param processor the processor for the command observer
	 */
	void observeCommand(CommandObserverProcessor processor);

}
