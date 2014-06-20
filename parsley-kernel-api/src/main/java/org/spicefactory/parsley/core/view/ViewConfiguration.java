package org.spicefactory.parsley.core.view;

import java.awt.Component;

import javax.annotation.Nullable;

/**
 * Represents the configuration for a single target in a view to get processed by the nearest Context in the view hierarchy above.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class ViewConfiguration {

	/**
	 * The target to get processed by the nearest Context in the view hierarchy.
	 */
	public final Object target;

	/**
	 * The view that demarcates the life-cycle of the target instance.
	 * <p>
	 * The view may be the same instance as the target itself or a different one that just controls the life-cycle. A typical example for the two
	 * properties pointing to different instances would be the target being a presentation model that should get added to the Context for the
	 * time the view it belongs to is on the stage.
	 */
	public final Component view;

	/**
	 * The configuration id to use to look up an object definition matching this target instance. If a definition has already been set for this
	 * configuration instance, this value will be ignored.
	 */
	public final String configId;

	/**
	 * The processor to use for the target instance.
	 * <p>
	 * This property may be null, in this case the default processor installed in the Context will be used.
	 */
	public @Nullable ViewProcessor processor;

	/**
	 * Creates a new instance.
	 * <p>
	 * If no target is specified the view itself is used as the target.
	 * @param view the view that controls the life-cycle of the target
	 * @param target the target to get processed by the Context
	 * @param configId the configuration id to use to fetch matching definitions from the Context
	 */
	public ViewConfiguration(Component view, @Nullable Object target, @Nullable String configId) {
		this.view = view;
		this.target = target;
		this.configId = (configId != null) ? configId : (target instanceof Component) ? ((Component) target).getName() : null;
	}

	/**
	 * Creates a new instance.
	 * <p>
	 * If no target is specified the view itself is used as the target.
	 * @param view the view that controls the life-cycle of the target
	 * @param target the target to get processed by the Context
	 */
	public ViewConfiguration(Component view, @Nullable Object target) {
		this(view, target, null);
	}

	/**
	 * Creates a new instance.
	 * @param view the view that controls the life-cycle of the target
	 */
	public ViewConfiguration(Component view) {
		this(view, null, null);
	}

	public interface CompleteHandler {
		void configurationComplete();
	}

}
