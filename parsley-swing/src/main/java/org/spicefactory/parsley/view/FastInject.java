package org.spicefactory.parsley.view;

import java.awt.Component;

import org.spicefactory.parsley.core.view.ViewConfiguration.CompleteHandler;
import org.spicefactory.parsley.core.view.ViewLifecycle;

/**
 * Provides a fluent API for injecting a managed object from the nearest Context in the view hierarchy into a view without reflecting on the
 * view.
 * <p>
 * This API can be used in Java components (Swing) as an alternative to the <code>FastInject</code> FXML annotation for FXML components.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public class FastInject {

	private final Component view;

	private Object target;
	private boolean reuse;
	private boolean autoremove;
	private ViewLifecycle lifecycle;
	private String property;
	private Class<?> type;
	private String name;
	private CompleteHandler completeHandler;

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	/**
	 * The view that demarcates the life-cycle of the target instance to inject into. The view may be the same instance as the target itself or a
	 * different one that just controls the life-cycle. A typical example for the two properties pointing to different instances would be the
	 * target being a presentation model that should get added to the Context for the time the view it belongs to is on the stage.
	 * @param view the view that demarcates the life-cycle of the target instance
	 * @return a new Configure instance for further setup options
	 */
	public static FastInject view(Component view) {
		return new FastInject(view);
	}

	/**
	 * The target to inject into.
	 * @param target the target to get processed by the nearest Context in the view hierarchy
	 * @return this FastInject instance for method chaining
	 */
	public FastInject target(Object target) {
		this.target = target;
		return this;
	}

	/**
	 * Indicates whether the target instance will be reused in subsequent life-cycles of the view. When set to false the injection will only be
	 * processed once. This value should be true if the application keeps instances of the view in memory and adds them back to the stage later.
	 * It should be false if the view will get garbage collected once it has been removed from the stage.
	 * @param reuse indicates whether the target instance will be reused in subsequent life-cycles of the view
	 * @return this FastInject instance for method chaining
	 */
	public FastInject reuse(boolean value) {
		this.reuse = value;
		return this;
	}

	/**
	 * Indicates whether the injected object should be removed from the Context when the view is removed from the stage and the injected object
	 * is a DynamicObject. Only has an effect when no custom <code>ViewLifecycle</code> has been set.
	 * @param autoremove Indicates whether the injected object should be removed from the Context when the view is removed from the stage and the
	 *            injected object is a DynamicObject
	 * @return this FastInject instance for method chaining
	 */
	public FastInject autoremove(boolean value) {
		this.autoremove = value;
		return this;
	}

	/**
	 * The instance that controls the life-cycle of the view. This property may be null, in this case the default life-cycle installed in the
	 * Context for the type of view of this configuration will be used.
	 * @param lifecycle the instance that controls the life-cycle of the view
	 * @return this FastInject instance for method chaining
	 */
	public FastInject lifecycle(ViewLifecycle lifecycle) {
		this.lifecycle = lifecycle;
		return this;
	}

	/**
	 * The name of the property of the target to inject into.
	 * @param definition name of the property of the target to inject into
	 * @return this FastInject instance for method chaining
	 */
	public FastInject property(String property) {
		this.property = property;
		return this;
	}

	/**
	 * The type of the object to inject from the nearest Parsley Context. FastInject can do either injection by type or by id. Thus either this
	 * method or <code>objectId</code> must be called.
	 * @param type the type of the object to inject from the nearest Parsley Context
	 * @return this FastInject instance for method chaining
	 */
	public FastInject type(Class<?> type) {
		this.type = type;
		return this;
	}

	/**
	 * The type of the object to inject from the nearest Parsley Context. FastInject can do either injection by type or by id. Thus either this
	 * method or <code>objectId</code> must be called.
	 * @param type the type of the object to inject from the nearest Parsley Context
	 * @param name the name of the object to inject from the nearest Parsley Context
	 * @return this FastInject instance for method chaining
	 */
	public FastInject type(Class<?> type, String name) {
		this.type = type;
		this.name = name;
		return this;
	}

	/**
	 * A callback to invoke when the injection has been completed.
	 * @param callback a callback to invoke when the injection has been completed
	 * @return this FastInject instance for method chaining
	 */
	public FastInject complete(CompleteHandler callback) {
		this.completeHandler = callback;
		return this;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	private FastInject(Component view) {
		this.view = view;
	}
}
