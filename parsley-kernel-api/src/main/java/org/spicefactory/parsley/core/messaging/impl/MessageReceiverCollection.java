package org.spicefactory.parsley.core.messaging.impl;

import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import org.spicefactory.parsley.core.messaging.MessageReceiverKind;
import org.spicefactory.parsley.core.messaging.receiver.MessageReceiver;

/**
 * A collection of message receivers for a particular message type.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
//Package-private.
final class MessageReceiverCollection {

	private final Class<?> messageType;
	private final Map<MessageReceiverKind, Set<MessageReceiver>> anySelector;
	private final Map<MessageReceiverKind, Set<MessageReceiver>> byValue;
	private final Map<MessageReceiverKind, Set<MessageReceiver>> byType;

	// Package-private.
	MessageReceiverCollection(Class<?> messageType) {
		this.messageType = messageType;
		this.anySelector = new EnumMap<MessageReceiverKind, Set<MessageReceiver>>(MessageReceiverKind.class);
		this.byValue = new EnumMap<MessageReceiverKind, Set<MessageReceiver>>(MessageReceiverKind.class);
		this.byType = new EnumMap<MessageReceiverKind, Set<MessageReceiver>>(MessageReceiverKind.class);
	}

	/**
	 * The type of message receivers in this collection are interested in.
	 */
	// Package-private.
	Class<?> messageType() {
		return messageType;
	}

	/**
	 * Adds a receiver to this collection.
	 * @param kind the kind of the receiver
	 * @param receiver the receiver to add
	 */
	// Package-private.
	void addReceiver(MessageReceiverKind kind, MessageReceiver receiver) {
		getReceiverMap(receiver.selector()).get(kind).add(receiver);
	}

	/**
	 * Removes a receiver from this collection.
	 * @param kind the kind of the receiver
	 * @param receiver the receiver to remove
	 */
	// Package-private.
	void removeReceiver(MessageReceiverKind kind, MessageReceiver receiver) {
		getReceiverMap(receiver.selector()).get(kind).remove(receiver);
	}

	/**
	 * Returns <tt>true</tt> if this map contains no key-value mappings.
	 * @return <tt>true</tt> if this map contains no key-value mappings
	 */
	// Package-private.
	boolean isEmpty() {
		return anySelector.isEmpty() && byType.isEmpty() && byValue.isEmpty();
	}

	/**
	 * Returns all receivers of a particular kind that match for the specified selector value.
	 * @param kind the kind of receiver to fetch
	 * @param selectorValue the value of the selector property
	 * @return all receivers of a particular kind that match for the specified selector value
	 */
	// Package-private.
	Set<MessageReceiver> getReceiversBySelectorValue(MessageReceiverKind kind, @Nullable Object selectorValue) {
		final Set<MessageReceiver> filtered = new HashSet<MessageReceiver>();

		if (selectorValue == null) {
			final Set<MessageReceiver> any = anySelector.get(kind);
			return Collections.unmodifiableSet((any == null) ? filtered : any);
		}

		final Set<MessageReceiver> receivers = (selectorValue instanceof Class<?>) ? byType.get(kind) : byValue.get(kind);

		if (receivers != null) {
			for (MessageReceiver receiver : receivers) {
				if (selectorValue == receiver.selector()) { // TODO: Check the use of '=='.
					filtered.add(receiver);
				}
			}
		}

		return addReceiversMatchingAnySelector(kind, filtered);
	}

	private Set<MessageReceiver> addReceiversMatchingAnySelector(MessageReceiverKind kind, Set<MessageReceiver> receivers) {
		final Set<MessageReceiver> any = anySelector.get(kind);
		if (any == null || any.size() == 0) {
			return Collections.unmodifiableSet(receivers);
		}

		final Set<MessageReceiver> result = new HashSet<MessageReceiver>(receivers);
		result.addAll(any);
		return Collections.unmodifiableSet(result);
	}

	private Map<MessageReceiverKind, Set<MessageReceiver>> getReceiverMap(@Nullable Object selector) {
		return (selector == null) ? anySelector : ((selector instanceof Class<?>) ? byType : byValue);
	}
}
