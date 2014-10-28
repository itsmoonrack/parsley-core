package org.spicefactory.parsley.core.view;

/**
 * A ViewManager is responsible for dynamically wiring views to a Context and other view related tasks. One or more view roots (any kind of
 * java.awt.Container) can be associated with a ViewManager. The manager then listens for bubbling events dispatched by children of any of the
 * view roots to signal that they want to be added to a Context.
 * <p>
 * In a simple application there is often just one view root associated with each Context. But in some use cases, in particular when using
 * javax.swing.JFrame or native java.awt.Window, additional view roots must be connected to a Context, since these pop-ups and windows build a
 * disconnected view hierarchy.
 * </p>
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface ViewManager {

	/**
	 * Adds a view root to this manager.
	 * <p>
	 * The manager is then responsible to listen to bubbling events from view components that wish to be wired to the Context or from a
	 * ContextBuilder that wishes to know the parent Context and the ClassLoader.
	 * @param view the view root to add to the manager
	 */
	void addViewRoot(Object view);

	/**
	 * Removes a view root from this manager.
	 * <p>
	 * The manager should immediately stop to listen to bubbling events from children of the specified view root.
	 * @param view the view root to remove from the manager
	 */
	void removeViewRoot(Object view);

}
