package org.spicefactory.parsley.core.events;

import java.awt.AWTEvent;

import org.spicefactory.parsley.core.bootstrap.BootstrapConfig;
import org.spicefactory.parsley.core.context.Context;

/**
 * Event that fires when a new Context gets configured that is associated with a view root.
 * <p>
 * The event allows listeners in the view hierarchy above to transparently add configuration artifacts before the Context gets built.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public class ContextConfigurationEvent extends AWTEvent {

	/**
	 * Constant for the type of bubbling event fired when a new Context gets configured that is associated with a view root. The event allows
	 * listeners in the view hierarchy above to transparently add configuration artifacts before the Context gets built.
	 */
	public static final int CONFIGURE_CONTEXT = 2000;

	public ContextConfigurationEvent(BootstrapConfig config) {
		super(config, CONFIGURE_CONTEXT);
	}

	/**
	 * The parent Context found in the view hierarchy.
	 */
	public Context viewParent;

	/**
	 * The configuration to be used for the new Context.
	 */
	public BootstrapConfig getConfig() {
		return (BootstrapConfig) getSource();
	}

}
