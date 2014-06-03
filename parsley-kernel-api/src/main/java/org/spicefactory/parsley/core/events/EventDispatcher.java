package org.spicefactory.parsley.core.events;

import java.util.EventListener;
import java.util.EventObject;
import java.util.Vector;

/**
 * This class represents an event dispatcher object, or "data" in the model-view paradigm. It can be sub-classed to represent an object that the
 * application wants to have observed.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 * @param <L>
 * @param <E>
 */
public abstract class EventDispatcher<L extends EventListener, E extends EventObject> {

	private final transient Vector<L> listeners = new Vector<L>();

	/**
	 * Adds the specified event listener to receive events from this implementation.
	 * @param l
	 */
	public synchronized void addEventListener(L l) {
		if (l == null) {
			throw new NullPointerException();
		}
		if (!listeners.contains(l)) {
			listeners.addElement(l);
		}
	}

	/**
	 * Removes the specified event listener so it no longer receives events from this implementation.
	 * @param l
	 */
	public synchronized void removeEventListener(L l) {
		listeners.removeElement(l);
	}

	protected void dispatchEvent(E e) {
		Object[] arrLocal = new Object[listeners.size()];

		synchronized (this) {
			arrLocal = listeners.toArray(arrLocal);
		}

		for (int i = arrLocal.length; i >= 0; i--) {
			processEvent((L) arrLocal[i], e);
		}
	}

	protected abstract void processEvent(L l, E e);

}
