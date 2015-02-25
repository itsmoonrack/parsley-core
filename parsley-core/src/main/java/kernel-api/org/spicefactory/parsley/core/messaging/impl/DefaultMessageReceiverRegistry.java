package org.spicefactory.parsley.core.messaging.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import org.spicefactory.parsley.core.messaging.MessageReceiverKind;
import org.spicefactory.parsley.core.messaging.MessageReceiverRegistry;
import org.spicefactory.parsley.core.messaging.receiver.CommandObserver;
import org.spicefactory.parsley.core.messaging.receiver.MessageExceptionHandler;
import org.spicefactory.parsley.core.messaging.receiver.MessageReceiver;
import org.spicefactory.parsley.core.messaging.receiver.MessageTarget;

@Singleton
public class DefaultMessageReceiverRegistry implements MessageReceiverRegistry {

	private final Map<Class<?>, MessageReceiverCollection> receivers;
	private final Map<Class<?>, DefaultMessageReceiverCache> selectionCache;

	public DefaultMessageReceiverRegistry() {
		// TODO: Check maps implementation + concurrency.
		receivers = new HashMap<Class<?>, MessageReceiverCollection>();
		selectionCache = new HashMap<Class<?>, DefaultMessageReceiverCache>();
	}

	/**
	 * Returns the selection of receivers that match the specified message type.
	 * @param messageType the message type to match against
	 * @return the selection of receivers that match the specified message type
	 */
	public DefaultMessageReceiverCache getSelectionCache(Class<?> messageType) {
		DefaultMessageReceiverCache receiverSelection = selectionCache.get(messageType);

		if (receiverSelection == null) {
			List<MessageReceiverCollection> collections = new ArrayList<MessageReceiverCollection>();
			for (MessageReceiverCollection collection : receivers.values()) {
				if (collection.getMessageType().isAssignableFrom(messageType)) {
					collections.add(collection);
				}
			}
			receiverSelection = new DefaultMessageReceiverCache(messageType, collections);
			selectionCache.put(messageType, receiverSelection);
		}

		return receiverSelection;
	}

	@Override
	public void addTarget(MessageTarget target) {
		addReceiver(MessageReceiverKind.TARGET, target);
	}

	@Override
	public void removeTarget(MessageTarget target) {
		removeReceiver(MessageReceiverKind.TARGET, target);
	}

	@Override
	public void addExceptionHandler(MessageExceptionHandler handler) {
		addReceiver(MessageReceiverKind.EXCEPTION_HANDLER, handler);
	}

	@Override
	public void removeExceptionHandler(MessageExceptionHandler handler) {
		removeReceiver(MessageReceiverKind.EXCEPTION_HANDLER, handler);
	}

	@Override
	public void addCommandObserver(CommandObserver observer) {
		addReceiver(observer.getKind(), observer);
	}

	@Override
	public void removeCommandObserver(CommandObserver observer) {
		removeReceiver(observer.getKind(), observer);
	}

	///////////////////////////////////////////////////////////////////////////////
	// Internals.
	///////////////////////////////////////////////////////////////////////////////

	private void addReceiver(MessageReceiverKind kind, MessageReceiver receiver) {
		MessageReceiverCollection collection = receivers.get(receiver.getType());
		if (collection == null) {
			collection = new MessageReceiverCollection(receiver.getType());
			receivers.put(receiver.getType(), collection);
			for (DefaultMessageReceiverCache cache : selectionCache.values()) {
				cache.checkNewCollection(collection);
			}
		}
		collection.addReceiver(kind, receiver);
	}

	private void removeReceiver(MessageReceiverKind kind, MessageReceiver receiver) {
		MessageReceiverCollection collection = receivers.get(receiver.getType());
		if (collection == null) {
			return;
		}
		collection.removeReceiver(kind, receiver);
		if (collection.isEmpty()) {
			receivers.remove(collection);
		}
	}

}
