package org.spicefactory.parsley.core.bootstrap;

import java.awt.Container;

import org.spicefactory.parsley.core.messaging.MessageSettings;
import org.spicefactory.parsley.core.view.ViewSettings;

/**
 * Represents the configuration to be used to create a new Context.
 * <p>
 * These configurations are applied hierarchical, meaning that anything not explicitly set on this instance will be fetched from the
 * configuration of the parent Context or, in case there is no parent or a particular option is not explicitly set on the parent, from default
 * configuration.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface BootstrapConfig {

	/**
	 * The initial view root to be used for view wiring.
	 * <p>
	 * Additional view roots can be added later through <code>viewManager.addViewRoot()</code>.
	 */
	Container viewRoot();

	/**
	 * The settings for the ViewManager of the new Context.
	 */
	ViewSettings viewSettings();

	/**
	 * The settings for the MessageRouter of the new Context.
	 */
	MessageSettings messageSettings();

	/**
	 * Specifies whether the new Context should automatically find a parent Context in the view hierarchy above the view root. The default is
	 * true. When a parent is found in the view hierarchy it will get added to the list of Contexts specified explicitly on this instance.
	 */
	boolean findParentInView();

}
