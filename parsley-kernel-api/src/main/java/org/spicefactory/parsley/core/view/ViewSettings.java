package org.spicefactory.parsley.core.view;

public @interface ViewSettings {

	/**
	 * Indicates whether the Context should be automatically destroyed when the last view root is removed from the ViewManager.
	 * <p>
	 * Defaults to <code>true</code>.
	 * @return
	 */
	boolean autoDestroyContext() default true;

	/**
	 * Indicates whether view roots should be automatically removed from the ViewManager when they are removed from the stage.
	 * <p>
	 * Defaults to <code>true</code>.
	 * <p>
	 * When set to false view roots must dispatch a custom <code>"removeView"</code> event to signal that they wish to be removed from the
	 * ViewManager. Finally they can be removed explicitly with <code>ViewManager.removeViewRoot</code> independent of the value for this
	 * property.
	 */
	boolean autoRemoveViewRoots() default true;

	/**
	 * Returns all view root handlers that were registered for these settings.
	 */
	Class<? extends ViewRootHandler>[] viewRootHandlers() default {};

}
