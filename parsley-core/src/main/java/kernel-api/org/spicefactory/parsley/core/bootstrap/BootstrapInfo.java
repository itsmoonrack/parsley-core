package org.spicefactory.parsley.core.bootstrap;

import org.spicefactory.parsley.core.context.Context;
import org.spicefactory.parsley.core.messaging.MessageRouter;
import org.spicefactory.parsley.core.messaging.MessageSettings;
import org.spicefactory.parsley.core.scope.ScopeManager;
import org.spicefactory.parsley.core.view.ViewManager;
import org.spicefactory.parsley.core.view.ViewSettings;

/**
 * Represents the environment for a single Context building process in a read-only way.
 * <p>
 * Implementations of this interface get passed to kernel services that implement the <code>InitializingService</code> interface. They can be
 * used to get access to collaborating services or other environment settings.
 * </p>
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface BootstrapInfo {

	/**
	 * The settings for the ViewManager of the new Context.
	 */
	ViewSettings viewSettings();

	/**
	 * The settings for the MessageRouter of the new Context.
	 */
	MessageSettings messageSettings();

	/**
	 * The Context under construction.
	 */
	Context context();

	/**
	 * The service that implements the messaging system.
	 */
	MessageRouter messageRouter();

	/**
	 * The service that manages all aspects of view wiring.
	 * <p>
	 * Normally only needed by the Context implementation.
	 */
	ViewManager viewManager();

	/**
	 * The service that manages the scopes for a particular Context.
	 * <p>
	 * Normally only needed by the Context implementation.
	 */
	ScopeManager scopeManager();

}
