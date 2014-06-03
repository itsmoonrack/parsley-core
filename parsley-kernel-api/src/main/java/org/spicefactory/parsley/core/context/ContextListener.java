package org.spicefactory.parsley.core.context;

import java.util.EventListener;

import org.spicefactory.parsley.core.events.ContextEvent;

/**
 * The listener interface for receiving context events.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface ContextListener extends EventListener {

	/**
	 * Invoked when the context has been destroyed.
	 * <p>
	 * At this point all methods annotated with @Destroy on objects managed by this context has been invoked and any child Context instances were
	 * destroyed, too.
	 */
	void contextDestroyed(ContextEvent e);

}
