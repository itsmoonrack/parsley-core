package org.spicefactory.parsley.core.messaging.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.spicefactory.lib.event.AbstractEventDispatcher;
import org.spicefactory.lib.event.Event;
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
class MessageReceiverCollection extends AbstractEventDispatcher<CollectionListener, CollectionEvent> {

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	MessageReceiverCollection(Class<?> messageType) {
		this.messageType = messageType;
		this.anySelector = new EnumMap<MessageReceiverKind, List<MessageReceiver>>(MessageReceiverKind.class);
		this.byValue = new EnumMap<MessageReceiverKind, List<MessageReceiver>>(MessageReceiverKind.class);
	}

	/**
	 * The type of message receivers in this collection are interested in.
	 */
	Class<?> messageType() {
		return messageType;
	}

	/**
	 * Adds a receiver to this collection.
	 * @param kind the kind of the receiver
	 * @param receiver the receiver to add
	 */
	void addReceiver(MessageReceiverKind kind, MessageReceiver receiver) {
		Map<MessageReceiverKind, List<MessageReceiver>> messageReceiversMap = getReceiverMap(receiver.selector());
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
		Map<MessageReceiverKind, List<MessageReceiver>> messageReceiversMap = getReceiverMap(receiver.selector());
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
		return anySelector.isEmpty() && byValue.isEmpty();
	}

	/**
	 * Returns all receivers of a particular kind that match for the specified selector value.
	 * @param kind the kind of receiver to fetch
	 * @param selectorValue the value of the selector property
	 * @return all receivers of a particular kind that match for the specified selector value
	 */
	List<MessageReceiver> getReceiversBySelectorValue(MessageReceiverKind kind, int selectorValue) {
		if (selectorValue == Selector.NONE) {
			final List<MessageReceiver> any = anySelector.get(kind);
			return Collections.unmodifiableList((any == null) ? new ArrayList<MessageReceiver>() : any);
		}

		final List<MessageReceiver> filtered = new ArrayList<MessageReceiver>();
		final List<MessageReceiver> receivers = byValue.get(kind);

		if (receivers != null) {
			for (MessageReceiver receiver : receivers) {
				if (selectorValue == receiver.selector()) {
					filtered.add(receiver);
				}
			}
		}

		return addReceiversMatchingAnySelector(kind, filtered);
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	private final Class<?> messageType;
	private final Map<MessageReceiverKind, List<MessageReceiver>> byValue;
	private final Map<MessageReceiverKind, List<MessageReceiver>> anySelector;

	private List<MessageReceiver> addReceiversMatchingAnySelector(MessageReceiverKind kind, List<MessageReceiver> receivers) {
		final List<MessageReceiver> any = anySelector.get(kind);
		if (any == null || any.size() == 0) {
			return Collections.unmodifiableList(receivers);
		}

		final List<MessageReceiver> result = new ArrayList<MessageReceiver>(receivers);
		result.addAll(any);
		return Collections.unmodifiableList(result);
	}

	private Map<MessageReceiverKind, List<MessageReceiver>> getReceiverMap(int selector) {
		return Selector.NONE == selector ? anySelector : byValue;
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
