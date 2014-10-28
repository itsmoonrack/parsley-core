package org.spicefactory.parsley.core.bootstrap;

import org.spicefactory.parsley.core.view.ViewSettings;

/**
 * Holds the instance that provides access to the global bootstrap configuration defaults for all settings and services that are not configured
 * explicitly for a particular Context.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
@ViewSettings
public class BootstrapDefaults {

	static BootstrapConfig defaults;

	public static BootstrapConfig config() {
		//		if (defaults == null) {
		//			defaults = new DefaultBootstrapConfig();
		//
		//			defaults.services.bootstrapManager.setImplementation(DefaultBootstrapManager.class);
		//			defaults.services.context.setImplementation(DefaultContext.class, ViewManager.class, Context[].class);
		//		}
		return defaults;
	}

}
