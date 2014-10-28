package org.spicefactory.parsley.core.events;

import org.spicefactory.lib.event.Event;
import org.spicefactory.parsley.core.context.Context;

/**
 * Event that fires when a Context changes its internal state.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public class ContextEvent extends Event {

	private static final long serialVersionUID = -4487196234461520013L;

	/**
	 * Constant for the type of event fired when a Context instance was destroyed.
	 */
	public static final int DESTROYED = 0x08;

	public ContextEvent(Context source, int type) {
		super(source, type);
	}

	public Context getContext() {
		return (Context) source;
	}

}
