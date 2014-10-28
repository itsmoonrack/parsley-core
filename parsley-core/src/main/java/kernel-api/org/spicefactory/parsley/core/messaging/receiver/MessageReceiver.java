package org.spicefactory.parsley.core.messaging.receiver;

/**
 * Base interface for all types of message receivers a MessageRouter handles.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface MessageReceiver {

	/**
	 * The class or interface of the message.
	 * <p>
	 * In case of a command observer this may also be interpreted as the type of the command, depending on the kind of receiver.
	 */
	Class<?> type();

	/**
	 * An optional selector value to be used for selecting matching messages.
	 */
	String selector();

	/**
	 * The execution order for this receiver.
	 * <p>
	 * Will be processed in ascending order. The default is <code>Integer.MAX_VALUE</code>.
	 * </p>
	 */
	int order();

}
