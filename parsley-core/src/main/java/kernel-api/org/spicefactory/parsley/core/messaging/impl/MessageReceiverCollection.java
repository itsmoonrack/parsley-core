package org.spicefactory.parsley.core.messaging.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.spicefactory.lib.event.Event;
import org.spicefactory.lib.event.EventDispatcher;
import org.spicefactory.lib.event.EventListener;
import org.spicefactory.parsley.core.messaging.MessageReceiverKind;
import org.spicefactory.parsley.core.messaging.Selector;
import org.spicefactory.parsley.core.messaging.impl.MessageReceiverCollection.CollectionEvent;
import org.spicefactory.parsley.core.messaging.impl.MessageReceiverCollection.CollectionListener;
import org.spicefactory.parsley.core.messaging.receiver.MessageReceiver;

/**
 * A collection of message receivers for a particular message type.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
//Package-private.
class MessageReceiverCollection extends EventDispatcher<CollectionListener, CollectionEvent> {

	private final Class<?> messageType;
	private final Map<MessageReceiverKind, List<MessageReceiver>> byType;
	private final Map<MessageReceiverKind, List<MessageReceiver>> byValue;
	private final Map<MessageReceiverKind, List<MessageReceiver>> anySelector;

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	MessageReceiverCollection(Class<?> messageType) {
		this.messageType = messageType;
		this.anySelector = Collections.synchronizedMap(new EnumMap<MessageReceiverKind, List<MessageReceiver>>(MessageReceiverKind.class));
		this.byValue = Collections.synchronizedMap(new EnumMap<MessageReceiverKind, List<MessageReceiver>>(MessageReceiverKind.class));
		this.byType = Collections.synchronizedMap(new EnumMap<MessageReceiverKind, List<MessageReceiver>>(MessageReceiverKind.class));
	}

	/**
	 * The type of message receivers in this collection are interested in.
	 */
	Class<?> getMessageType() {
		return messageType;
	}

	/**
	 * Adds a receiver to this collection.
	 * @param kind the kind of the receiver
	 * @param receiver the receiver to add
	 */
	void addReceiver(MessageReceiverKind kind, MessageReceiver receiver) {
		Map<MessageReceiverKind, List<MessageReceiver>> messageReceiversMap = getReceiverMap(receiver);
		List<MessageReceiver> messageReceivers = messageReceiversMap.get(kind);
		if (messageReceivers == null) {
			messageReceivers = new ArrayList<MessageReceiver>();
			messageReceiversMap.put(kind, messageReceivers);
		}
		messageReceivers.add(receiver);
		dispatchEvent(new CollectionEvent(CollectionEvent.CHANGE));
	}

	/**
	 * Removes a receiver from this collection.
	 * @param kind the kind of the receiver
	 * @param receiver the receiver to remove
	 */
	void removeReceiver(MessageReceiverKind kind, MessageReceiver receiver) {
		Map<MessageReceiverKind, List<MessageReceiver>> messageReceiversMap = getReceiverMap(receiver);
		List<MessageReceiver> messageReceivers = messageReceiversMap.get(kind);
		if (messageReceivers == null) {
			return;
		}
		messageReceivers.remove(receiver);
		dispatchEvent(new CollectionEvent(CollectionEvent.CHANGE));
	}

	/**
	 * Returns <tt>true</tt> if this map contains no key-value mappings.
	 * @return <tt>true</tt> if this map contains no key-value mappings
	 */
	boolean isEmpty() {
		return anySelector.isEmpty() && byValue.isEmpty() && byType.isEmpty();
	}

	/**
	 * Returns all receivers of a particular kind that match for the specified selector value.
	 * @param kind the kind of receiver to fetch
	 * @param selectorValue the value of the selector property
	 * @return all receivers of a particular kind that match for the specified selector value
	 */
	List<MessageReceiver> getReceiversBySelectorValue(MessageReceiverKind kind, Object selectorValue) {
		if (selectorValue.equals(Selector.NONE)) {
			final List<MessageReceiver> any = anySelector.get(kind);
			return any == null ? new ArrayList<MessageReceiver>() : Collections.unmodifiableList(any);
		}

		final List<MessageReceiver> receivers = selectorValue instanceof Class ? byType.get(kind) : byValue.get(kind);
		final List<MessageReceiver> filtered = new ArrayList<MessageReceiver>();

		if (receivers != null) {
			for (MessageReceiver receiver : receivers) {
				if (selectorValue.equals(receiver.getSelector())) {
					filtered.add(receiver);
				}
			}
		}

		return addReceiversMatchingAnySelector(kind, filtered);
	}

	List<MessageReceiver> getReceiversBySelectorType(MessageReceiverKind kind, Object selectorValue) {
		if (selectorValue.equals(Selector.NONE)) {
			final List<MessageReceiver> any = anySelector.get(kind);
			return any == null ? new ArrayList<MessageReceiver>() : Collections.unmodifiableList(any);
		}

		final List<MessageReceiver> receivers = byType.get(kind);
		final List<MessageReceiver> filtered = new ArrayList<MessageReceiver>();

		if (receivers != null) {
			for (MessageReceiver receiver : receivers) {
				Class<?> type = (Class<?>) receiver.getSelector();
				if (selectorValue.getClass().isAssignableFrom(type)) {
					filtered.add(receiver);
				}
			}
		}

		return addReceiversMatchingAnySelector(kind, filtered);
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	private List<MessageReceiver> addReceiversMatchingAnySelector(MessageReceiverKind kind, List<MessageReceiver> receivers) {
		final List<MessageReceiver> anys = anySelector.get(kind);
		if (anys == null || anys.size() == 0) {
			return receivers;
		}

		if (receivers.size() > 0) {
			receivers.addAll(anys);
			return receivers;
		}

		return anys;
	}

	private Map<MessageReceiverKind, List<MessageReceiver>> getReceiverMap(MessageReceiver receiver) {
		return receiver.getSelector() == null || receiver.getSelector().equals(Selector.NONE) ? anySelector
				: receiver.getSelector() instanceof Class ? byType : byValue;
	}

	// This will not be needed anymore in Java 1.8. with lamba.

	class CollectionEvent extends Event {

		private static final long serialVersionUID = 2949513804081228608L;

		public final static int CHANGE = 0x01;

		public CollectionEvent(int type) {
			super(type);
		}
	}

	interface CollectionListener extends EventListener<CollectionEvent> {

	}
}
