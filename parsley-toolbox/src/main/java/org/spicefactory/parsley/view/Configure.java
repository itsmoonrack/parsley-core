package org.spicefactory.parsley.view;

import java.awt.Component;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spicefactory.parsley.core.events.ViewConfigurationEvent;
import org.spicefactory.parsley.core.view.ViewConfiguration;
import org.spicefactory.parsley.core.view.ViewProcessor;

/**
 * Provides a fluent API for configuring a target in a view.
 * <p>
 * This API can be used in Java components (JavaFX or Swing) as an alternative to the <code>Configure</code> FXML tag for FXML components.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public final class Configure implements HierarchyListener {

	private final Logger logger = LoggerFactory.getLogger(Configure.class);

	/**
	 * The view that demarcates the life-cycle of the target instance to be configured.
	 * <p>
	 * The view may be the same instance as the target itself or a different one that just controls the life-cycle. A typical example for the two
	 * properties pointing to different instances would be the target being a presentation model that should get added to the Context for the
	 * time the view it belongs to is on the stage.
	 * @param view the view that demarcates the life-cycle of the target instance
	 * @return a new Configure instance for further setup options.
	 */
	public static Configure view(Component view) {
		return new Configure(view);
	}

	/**
	 * The target to get processed by the nearest Context in the view hierarchy.
	 * @param target the target to get processed by the nearest Context in the view hierarchy.
	 * @return this Configure instance for method chaining
	 */
	public Configure target(Object target) {
		this.target = target;
		return this;
	}

	/**
	 * The configuration id to use to look up an object definition matching this target instance. If a definition has already been set for this
	 * configuration instance, this value will be ignored.
	 * @param configId the configuration id to use to look up an object definition matching this target instance.
	 * @return this Configure instance for method chaining
	 */
	public Configure configId(String configId) {
		this.configId = configId;
		return this;
	}

	/**
	 * Executes this view configuration at the nearest Context in the view hierarchy.
	 */
	public void execute() {
		if (view.getParent() == null) {
			view.addHierarchyListener(this);
		} else {
			dispatchEvent();
		}
	}

	@Override
	public void hierarchyChanged(HierarchyEvent evt) {
		if ((evt.getChangeFlags() & HierarchyEvent.PARENT_CHANGED) != 0) {
			view.removeHierarchyListener(this);
			dispatchEvent();
		}
	}

	private void dispatchEvent() {
		ViewConfiguration config = new ViewConfiguration(view, target, configId);
		config.processor = processor;
		ViewConfigurationEvent event = ViewConfigurationEvent.forConfigurations(new ViewConfiguration[] {config}, source);
		view.dispatchEvent(event);
		if (!event.received()) {
			logger.warn("View configuration could not be processed for target {}: no Context found in view hierarchy.", config.target);
		}
	}

	private final Component view;
	private Object target;

	private ViewProcessor processor;
	private String configId;

	// Private
	private Configure(Component view) {
		this.view = view;
	}

}
