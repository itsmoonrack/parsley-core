package org.spicefactory.parsley.core.events;

import java.util.EventObject;

import org.spicefactory.parsley.core.context.Context;

/**
 * Event that fires when a Context changes its internal state.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public class ContextEvent extends EventObject {

	/**
	 * Constant for the type of event fired when a Context instance was destroyed.
	 * @param source
	 */
	public static final int DESTROYED = 8;

	private transient int id;

	public ContextEvent(Context source, int id) {
		super(source);
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public Context getContext() {
		return (Context) source;
	}

}
