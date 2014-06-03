package org.spicefactory.parsley.core.messaging.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.spicefactory.parsley.core.messaging.MessageReceiverKind;
import org.spicefactory.parsley.core.messaging.MessageReceiverRegistry;
import org.spicefactory.parsley.core.messaging.receiver.CommandObserver;
import org.spicefactory.parsley.core.messaging.receiver.MessageErrorHandler;
import org.spicefactory.parsley.core.messaging.receiver.MessageReceiver;
import org.spicefactory.parsley.core.messaging.receiver.MessageTarget;

public class DefaultMessageReceiverRegistry implements MessageReceiverRegistry {

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
				if (messageType.isAssignableFrom(collection.messageType())) { // TODO: Check order of isAssignableFrom.
					collections.add(collection);
				}
			}
			receiverSelection = new DefaultMessageReceiverCache(messageType, collections);
			selectionCache.put(messageType, receiverSelection);
			//			domainManager.addPurgeHandler(messageType.getClassLoader(), clearDomainCache, messageType);
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
	public void addErrorHandler(MessageErrorHandler handler) {
		addReceiver(MessageReceiverKind.ERROR_HANDLER, handler);
	}

	@Override
	public void removeErrorHandler(MessageErrorHandler handler) {
		removeReceiver(MessageReceiverKind.ERROR_HANDLER, handler);
	}

	@Override
	public void addCommandObserver(CommandObserver observer) {
		addReceiver(observer.kind(), observer);
	}

	@Override
	public void removeCommandObserver(CommandObserver observer) {
		removeReceiver(observer.kind(), observer);
	}

	///////////////////////////////////////////////////////////////////////////////
	// Internals.
	///////////////////////////////////////////////////////////////////////////////

	private final Map<Class<?>, MessageReceiverCollection> receivers;
	private final Map<Class<?>, DefaultMessageReceiverCache> selectionCache;

	private void addReceiver(MessageReceiverKind kind, MessageReceiver receiver) {
		MessageReceiverCollection collection = receivers.get(receiver.type());
		if (collection == null) {
			collection = new MessageReceiverCollection(receiver.type());
			receivers.put(receiver.type(), collection);
			for (DefaultMessageReceiverCache cache : selectionCache.values()) {
				cache.checkNewCollection(collection);
			}
		}
		collection.addReceiver(kind, receiver);
	}

	private void removeReceiver(MessageReceiverKind kind, MessageReceiver receiver) {
		MessageReceiverCollection collection = receivers.get(receiver.type());
		if (collection == null) {
			return;
		}
		collection.removeReceiver(kind, receiver);
		if (collection.isEmpty()) {
			receivers.remove(collection);
		}
	}

}
