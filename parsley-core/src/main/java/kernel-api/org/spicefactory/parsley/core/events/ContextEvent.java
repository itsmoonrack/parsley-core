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
	 * Constant for the type of event fired when a Context instance was configured.
	 * <p>
	 * After this event has fired all configuration has been processed and all <code>ObjectDefinition</code> instances have been frozen and
	 * cannot be modified anymore. <code>Context.getObject</code> and other methods of the Context can now be called. But application code should
	 * be careful to fetch objects from the container before the <code>INITIALIZED</code> event because it could alter the sequence of
	 * asynchronously initializing objects. Nevertheless it is legal to fetch objects at this point because the asynchronously initializing
	 * objects might have dependencies themselves.
	 */
	public static final int CONFIGURED = 0x01;

	/**
	 * Constant for the type of event fired when a Context instance was initialized.
	 * <p>
	 * A Context is fully initialized if all asynchronous initializers for non-lazy singletons (if any) have completed and the parent Context (if
	 * set) is fully initialized too.
	 */
	public static final int INITIALIZED = 0x02;

	/**
	 * Constant for the type of event fired when a Context instance is on error.
	 */
	public static final int ERROR = 0x04;

	/**
	 * Constant for the type of event fired when a Context instance was destroyed.
	 */
	public static final int DESTROYED = 0x08;

	public ContextEvent(int type) {
		super(type);
	}

	public Context getContext() {
		return (Context) source;
	}

}
