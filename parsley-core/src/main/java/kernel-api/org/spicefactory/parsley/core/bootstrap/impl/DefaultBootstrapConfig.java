package org.spicefactory.parsley.core.bootstrap.impl;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spicefactory.parsley.core.bootstrap.BootstrapConfig;
import org.spicefactory.parsley.core.bootstrap.BootstrapProcessor;
import org.spicefactory.parsley.core.context.Context;
import org.spicefactory.parsley.core.events.ContextConfigurationEvent;
import org.spicefactory.parsley.core.view.impl.DefaultViewManager;

/**
 * Default implementation of BootstrapConfig.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public class DefaultBootstrapConfig extends BootstrapConfig {

	private final Logger logger = LoggerFactory.getLogger(DefaultBootstrapConfig.class);

	private final DefaultViewManager viewManager;

	@Inject
	DefaultBootstrapConfig(DefaultViewManager viewManager) {
		this.viewManager = viewManager;
	}

	BootstrapProcessor createProcessor() {
		findParent();
		return new DefaultBootstrapProcessor();
	}

	private void findParent() {
		Context viewParent;
		if (viewRoot != null) {
			if (viewRoot.getParent() == null) {
				logger.warn("Probably unable to look for parent Context in the view hierarchy, {} has not been added to the stage yet.",
						viewRoot);
			}

			ContextConfigurationEvent event = new ContextConfigurationEvent(this);
			viewRoot.dispatchEvent(event);

			if (findParentInView) {
				// A listener for the event dispatched in the previous statement
				// might have changed the findParentInView flag, therefore this is
				// the earliest time we can use it.
				viewParent = event.viewParent;
			}
		}
	}
}
