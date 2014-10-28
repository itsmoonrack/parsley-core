package org.spicefactory.parsley.core.bootstrap.impl;

import java.awt.Container;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spicefactory.parsley.core.bootstrap.BootstrapConfig;
import org.spicefactory.parsley.core.bootstrap.BootstrapProcessor;
import org.spicefactory.parsley.core.messaging.MessageSettings;
import org.spicefactory.parsley.core.view.ViewSettings;
import org.spicefactory.parsley.core.view.impl.DefaultViewManager;

/**
 * Default implementation of BootstrapConfig.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public class DefaultBootstrapConfig implements BootstrapConfig {

	private final Logger logger = LoggerFactory.getLogger(DefaultBootstrapConfig.class);

	private final DefaultViewManager viewManager;

	@Inject
	DefaultBootstrapConfig(DefaultViewManager viewManager) {
		this.viewManager = viewManager;
	}

	BootstrapProcessor createProcessor() {
		//		findParent();
		return new DefaultBootstrapProcessor();
	}

	@Override
	public Container viewRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ViewSettings viewSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MessageSettings messageSettings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean findParentInView() {
		// TODO Auto-generated method stub
		return false;
	}

	//	private void findParent() {
	//		Context viewParent;
	//		if (viewRoot != null) {
	//			if (viewRoot.getParent() == null) {
	//				logger.warn("Probably unable to look for parent Context in the view hierarchy, {} has not been added to the stage yet.",
	//						viewRoot);
	//			}
	//
	//			ContextConfigurationEvent event = new ContextConfigurationEvent(this);
	//			viewRoot.dispatchEvent(event);
	//
	//			if (findParentInView) {
	//				// A listener for the event dispatched in the previous statement
	//				// might have changed the findParentInView flag, therefore this is
	//				// the earliest time we can use it.
	//				viewParent = event.viewParent;
	//			}
	//		}
	//	}
}
