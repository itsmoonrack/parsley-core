package org.spicefactory.parsley.messaging.receiver;

/**
 * The base configuration for a message receiver.
 * <p>
 * Holds only the properties common to all receivers.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class MessageReceiverInfo {

	public MessageReceiverInfo() {
		// Do nothing.
	}

	public MessageReceiverInfo(Class<?> type, Object selector, int order) {
		this.type = type;
		this.selector = selector;
		this.order = order;
	}

	/**
	 * The type of the message the receiver listens to.
	 */
	public Class<?> type;

	/**
	 * An optional selector value to be used in addition to selecting messages by type.
	 * <p>
	 * Will be checked against the value of the property in the message marked with <code>@Selector</code> or against the event type if the
	 * message is an event and does not have a selector property specified explicitly.
	 * </p>
	 */
	public Object selector;

	/**
	 * The execution order for this receiver. Will be processed in ascending order.
	 * <p>
	 * The default is <code>Interger.MAX_VALUE</code>.
	 * </p>
	 */
	public int order;

}
