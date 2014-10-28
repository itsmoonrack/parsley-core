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
public abstract class AbstractEventDispatcher<L extends EventListener, E extends EventObject> implements EventDispatcher<L> {

	private final transient Vector<L> listeners = new Vector<L>();

	/* (non-Javadoc)
	 * @see org.spicefactory.parsley.core.events.EventDispatcher#addEventListener(L)
	 */
	@Override
	public synchronized void addEventListener(L l) {
		if (l == null) {
			throw new NullPointerException();
		}
		if (!listeners.contains(l)) {
			listeners.addElement(l);
		}
	}

	/* (non-Javadoc)
	 * @see org.spicefactory.parsley.core.events.EventDispatcher#removeEventListener(L)
	 */
	@Override
	public synchronized void removeEventListener(L l) {
		listeners.removeElement(l);
	}

	protected void dispatchEvent(E e) {
		Object[] arrLocal = new Object[listeners.size()];

		synchronized (this) {
			arrLocal = listeners.toArray(arrLocal);
		}

		for (int i = arrLocal.length - 1; i > 0; i--) {
			processEvent((L) arrLocal[i], e);
		}
	}

	protected abstract void processEvent(L l, E e);

}
