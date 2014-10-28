package org.spicefactory.parsley.core.messaging.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Nullable;

import org.spicefactory.parsley.core.messaging.MessageReceiverCache;
import org.spicefactory.parsley.core.messaging.MessageReceiverKind;
import org.spicefactory.parsley.core.messaging.Selector;
import org.spicefactory.parsley.core.messaging.impl.MessageReceiverCollection.CollectionEvent;
import org.spicefactory.parsley.core.messaging.impl.MessageReceiverCollection.CollectionListener;
import org.spicefactory.parsley.core.messaging.receiver.MessageReceiver;

/**
 * A cached selection of receivers for a particular message messageType and its sub-types. Will be used by the default MessageRouter
 * implementation as a performance optimization.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
// Package-private.
class DefaultMessageReceiverCache implements MessageReceiverCache, CollectionListener {

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	DefaultMessageReceiverCache(Class<?> type, List<MessageReceiverCollection> collections) {
		this.messageType = type;
		this.collections = collections;
		this.selectorMaps = new ConcurrentHashMap<MessageReceiverKind, SelectorMap>();
		this.selectorField = getSelectorField(type.getFields());
	}

	/**
	 * Checks whether the specified new collection matches the message messageType of this cache and adds it in that case.
	 * @param collection the new collection to check
	 */
	void checkNewCollection(MessageReceiverCollection collection) {
		if (messageType.isAssignableFrom(collection.messageType())) { // TODO: Check order of isAssignableFrom.
			collection.addEventListener(CollectionEvent.CHANGE, this);
			selectorMaps.clear();
			addListener(collection);
		}
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	@Override
	public List<MessageReceiver> getReceivers(MessageReceiverKind kind, String selector) {
		return getSelectorMap(kind).getReceivers(selector, messageType.getClassLoader());
	}

	@Override
	public String getSelectorValue(Object message) {
		if (selectorField != null) {
			selectorField.setAccessible(true);
			try {
				return (String) selectorField.get(message);
			}
			catch (Exception e) {
				// Ignores returns null.
			}
		}

		return null;
	}

	/**
	 * Releases this selection cache in case it is no longer used.
	 * <p>
	 * Usually only called by the framework when there is no Context left that uses the message type of this cache belongs to.
	 */
	public void dispose() {
		selectorMaps.clear();
		for (MessageReceiverCollection collection : collections) {
			collection.removeEventListener(CollectionEvent.CHANGE, this);
		}
		collections.clear();
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	private final Class<?> messageType;
	private final Field selectorField;
	private final List<MessageReceiverCollection> collections;

	private final ConcurrentMap<MessageReceiverKind, SelectorMap> selectorMaps;

	private SelectorMap getSelectorMap(MessageReceiverKind kind) {
		SelectorMap selectorMap = selectorMaps.get(kind);

		if (selectorMap == null) {
			selectorMap = new SelectorMap(kind);
			selectorMaps.put(kind, selectorMap);
		}

		return selectorMap;
	}

	private Field getSelectorField(Field[] fields) {
		for (Field field : fields) {
			if (field.isAnnotationPresent(Selector.class)) {
				return field;
			}
		}
		return null;
	}

	private void addListener(MessageReceiverCollection collection) {
		collection.addEventListener(CollectionEvent.CHANGE, this);
	}

	@Override
	public void process(CollectionEvent e) {
		selectorMaps.clear();
		MessageReceiverCollection collection = (MessageReceiverCollection) e.getSource();
		if (collection.isEmpty()) {
			collection.removeEventListener(CollectionEvent.CHANGE, this);
			collections.remove(collection);
		}
	}

	private class SelectorMap {

		private final MessageReceiverKind kind;
		private final Map<Object, List<MessageReceiver>> cache;

		public SelectorMap(MessageReceiverKind kind) {
			this.kind = kind;
			this.cache = new HashMap<Object, List<MessageReceiver>>();
		}

		public List<MessageReceiver> getReceivers(@Nullable String selector, ClassLoader loader) {
			if (selector == null || selector.getClass().isPrimitive()) {
				return getReceiversBySelectorValue(selector);
			} else {
				return getReceiversBySelectorType(selector, loader);
			}
		}

		private List<MessageReceiver> getReceiversBySelectorValue(String selector) {
			List<MessageReceiver> receivers = cache.get(selector);

			if (receivers == null) {
				receivers = new ArrayList<MessageReceiver>();
				for (MessageReceiverCollection collection : collections) {
					List<MessageReceiver> subset = collection.getReceiversBySelectorValue(kind, selector);
					receivers.addAll(subset);
				}
				cache.put(selector, receivers);
			}

			return receivers;
		}

		private List<MessageReceiver> getReceiversBySelectorType(String selector, ClassLoader loader) {
			return null;
		}
	}

}
