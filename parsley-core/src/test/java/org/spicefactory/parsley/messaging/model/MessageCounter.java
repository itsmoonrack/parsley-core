package org.spicefactory.parsley.messaging.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.spicefactory.parsley.core.messaging.Selector;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class MessageCounter {

	private final Map<Class<?>, MessagesByType> messages = new HashMap<Class<?>, MessagesByType>();
	private final List<Object> allMessages = new LinkedList<Object>();

	public void addMessage(Object message) {
		addMessage(message, Selector.NONE);
	}

	public void addMessage(Object message, Object selector) {
		allMessages.add(message);
		MessagesByType byType = messages.get(message.getClass());
		if (byType == null) {
			byType = new MessagesByType();
			messages.put(message.getClass(), byType);
		}
		byType.addMessage(message, selector);
	}

	public int getCount() {
		return getCount(null, Selector.NONE);
	}

	public int getCount(Class<?> type) {
		return getCount(type, Selector.NONE);
	}

	public int getCount(Class<?> type, Object selector) {
		if (type == null) {
			return allMessages.size();
		} else if (messages.containsKey(type)) {
			return messages.get(type).getCount(selector);
		} else {
			return 0;
		}
	}

	private class MessagesByType {

		private final Map<Object, List<Object>> messages = new HashMap<Object,  List<Object>>();
		private final List<Object> allMessages = new LinkedList<Object>();

		public void addMessage(Object message, Object selector) {
			allMessages.add(message);
			if (!selector.equals(Selector.NONE)) {
				List<Object> bySelector = messages.get(selector);
				if (bySelector == null) {
					bySelector = new LinkedList<Object>();
					messages.put(selector, bySelector);
				}
				bySelector.add(message);
			}
		}

		public int getCount(Object selector) {
			if (selector.equals(Selector.NONE)) {
				return allMessages.size();
			} else if (messages.containsKey(selector)) {
				return messages.get(selector).size();
			} else {
				return 0;
			}
		}

	}
}
