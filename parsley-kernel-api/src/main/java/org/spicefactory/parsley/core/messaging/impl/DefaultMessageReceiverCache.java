package org.spicefactory.parsley.core.messaging.impl;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.Nullable;

import org.spicefactory.parsley.core.messaging.MessageReceiverCache;
import org.spicefactory.parsley.core.messaging.MessageReceiverKind;
import org.spicefactory.parsley.core.messaging.Selector;
import org.spicefactory.parsley.core.messaging.receiver.MessageReceiver;

/**
 * A cached selection of receivers for a particular message messageType and its sub-types. Will be used by the default MessageRouter
 * implementation as a performance optimization.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
// Package-private.
class DefaultMessageReceiverCache implements MessageReceiverCache {

	// Package-private.
	DefaultMessageReceiverCache(Class<?> type, List<MessageReceiverCollection> collections) {
		this.messageType = type;
		this.collections = collections;
		this.selectorMaps = new ConcurrentHashMap<MessageReceiverKind, SelectorMap>();
		this.selectorField = getSelectorField(type.getFields());
	}

	@Override
	public Set<MessageReceiver> getReceivers(MessageReceiverKind kind, Object selector) {
		return getSelectorMap(kind).getReceivers(selector, messageType.getClassLoader());
	}

	@Override
	public Object getSelectorValue(Object message) {
		if (selectorField != null) {
			selectorField.setAccessible(true);
			try {
				return selectorField.get(message);
			}
			catch (Exception e) {
				// Ignores returns null.
			}
		}

		return null;
	}

	/**
	 * Checks whether the specified new collection matches the message messageType of this cache and adds it in that case.
	 * @param collection the new collection to check
	 */
	// Package-private.
	void checkNewCollection(MessageReceiverCollection collection) {
		if (messageType.isAssignableFrom(collection.messageType())) { // TODO: Check order of isAssignableFrom.
			collections.add(collection);
			selectorMaps.clear();
			//			addListener(collection);
		}
	}

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

	private class SelectorMap {

		private final MessageReceiverKind kind;
		private final Map<Object, Set<MessageReceiver>> cache;

		public SelectorMap(MessageReceiverKind kind) {
			this.kind = kind;
			this.cache = new HashMap<Object, Set<MessageReceiver>>();
		}

		public Set<MessageReceiver> getReceivers(@Nullable Object selector, ClassLoader loader) {
			if (selector == null || selector.getClass().isPrimitive()) {
				return getReceiversBySelectorValue(selector);
			} else {
				return getReceiversBySelectorType(selector, loader);
			}
		}

		private Set<MessageReceiver> getReceiversBySelectorValue(Object selector) {
			Set<MessageReceiver> receivers = cache.get(selector);

			if (receivers == null) {
				receivers = new HashSet<MessageReceiver>();
				for (MessageReceiverCollection collection : collections) {
					Set<MessageReceiver> subset = collection.getReceiversBySelectorValue(kind, selector);
					receivers.addAll(subset);
				}
				cache.put(selector, receivers);
			}

			return receivers;
		}

		private Set<MessageReceiver> getReceiversBySelectorType(Object selector, ClassLoader loader) {
			Class<?> selectorType = getSelectorType(selector, loader);
			return null;
		}

		private Class<?> getSelectorType(Object selector, ClassLoader loader) {
			return loader.loadClass(selector.getClass().getCanonicalName());
		}
	}

}
