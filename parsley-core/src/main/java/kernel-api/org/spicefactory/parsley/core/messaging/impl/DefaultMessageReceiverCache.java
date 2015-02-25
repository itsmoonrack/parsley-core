package org.spicefactory.parsley.core.messaging.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.spicefactory.lib.event.Event;
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

	private final Class<?> messageType;
	private final Field selectorField;
	private final List<MessageReceiverCollection> collections;

	private final ConcurrentMap<MessageReceiverKind, SelectorMap> selectorMaps;

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	DefaultMessageReceiverCache(Class<?> type, List<MessageReceiverCollection> collections) {
		this.messageType = type;
		this.collections = collections;
		this.selectorMaps = new ConcurrentHashMap<MessageReceiverKind, SelectorMap>();
		this.selectorField = getSelectorField(type);
	}

	/**
	 * Checks whether the specified new collection matches the message messageType of this cache and adds it in that case.
	 * @param collection the new collection to check
	 */
	void checkNewCollection(MessageReceiverCollection collection) {
		if (collection.getMessageType().isAssignableFrom(messageType)) {
			collection.addEventListener(CollectionEvent.CHANGE, this);
			selectorMaps.clear();
			addListener(collection);
		}
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	@Override
	public List<MessageReceiver> getReceivers(MessageReceiverKind kind, Object selector) {
		return getSelectorMap(kind).getReceivers(selector, messageType.getClassLoader());
	}

	@Override
	public int getSelectorValue(Object message) {
		if (selectorField != null) {
			selectorField.setAccessible(true);
			try {
				return (Integer) selectorField.get(message);
			}
			catch (Exception e) {
				// Ignores returns null.
			}
		}
		return Selector.NONE;
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

	private SelectorMap getSelectorMap(MessageReceiverKind kind) {
		SelectorMap selectorMap = selectorMaps.get(kind);

		if (selectorMap == null) {
			selectorMap = new SelectorMap(kind);
			selectorMaps.put(kind, selectorMap);
		}

		return selectorMap;
	}

	private Field getSelectorField(Class<?> type) {
		Class<?> c = type;

		while (c.getSuperclass() != null) {
			for (Field field : c.getDeclaredFields()) {
				if (field.isAnnotationPresent(Selector.class)) {
					return field;
				}
			}
			c = c.getSuperclass();
		}

		try {
			if (Event.class.isAssignableFrom(type)) {
				return type.getField("id");
			}
			return null;
		}
		catch (Exception e) {
			return null;
		}
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
		private final Map<Object, List<MessageReceiver>> byType;
		private final Map<Object, List<MessageReceiver>> byValue;

		public SelectorMap(MessageReceiverKind kind) {
			this.kind = kind;
			this.byType = new HashMap<Object, List<MessageReceiver>>();
			this.byValue = new HashMap<Object, List<MessageReceiver>>();
		}

		public List<MessageReceiver> getReceivers(Object selector, ClassLoader loader) {
			if (selector.equals(Selector.NONE) //
					|| selector instanceof String //
					|| selector instanceof Number //
					|| selector instanceof Class) {
				return getReceiversBySelectorValue(selector);
			} else {
				return getReceiversBySelectorType(selector, loader);
			}
		}

		private List<MessageReceiver> getReceiversBySelectorValue(Object selector) {
			List<MessageReceiver> receivers = byValue.get(selector);

			if (receivers == null) {
				receivers = new ArrayList<MessageReceiver>();
				for (MessageReceiverCollection collection : collections) {
					receivers.addAll(collection.getReceiversBySelectorValue(kind, selector));
				}
				byValue.put(selector, receivers);
			}

			return receivers;
		}

		private List<MessageReceiver> getReceiversBySelectorType(Object selector, ClassLoader loader) {
			Class<?> type = getSelectorType(selector, loader);
			List<MessageReceiver> receivers = byType.get(type);

			if (receivers == null) {
				receivers = new ArrayList<MessageReceiver>();
				for (MessageReceiverCollection collection : collections) {
					receivers.addAll(collection.getReceiversBySelectorType(kind, selector));
				}
				byType.put(type, receivers);
			}

			return receivers;
		}

		private Class<?> getSelectorType(Object selector, ClassLoader loader) {
			Class<?> c = null;
			if (!(selector instanceof Proxy && selector instanceof Number)) {
				// Cannot rely on Proxy subclasses to support the constructor property.
				// For Number instance constructor property always returns Number (never int).
				c = selector.getClass();
			}
			//			if (c == null) {
			//					c = loader.loadClass(selector.getClass().getCanonicalName());
			//			}
			return c;
		}
	}

}
