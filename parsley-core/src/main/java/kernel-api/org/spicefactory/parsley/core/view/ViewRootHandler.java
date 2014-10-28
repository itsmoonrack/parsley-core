package org.spicefactory.parsley.core.view;

import java.awt.Container;

import org.spicefactory.parsley.core.context.Context;

/**
 * A ViewRootHandler is responsible for dealing with one particular aspect of a ViewManager.
 * <p>
 * There are four default ViewRootHandler implementations automatically added to each ViewManager that deal with several built-in features like
 * view auto-wiring. But applications can easily add their own functionality through <code>ViewSettings.addViewRootHandler</code> or the
 * viewRootHandler attribute of the <code>@ViewSettings</code> annotation.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface ViewRootHandler {

	/**
	 * Initializes the handler and passes some collaborators.
	 * <p>
	 * Invoked once in the life-cycle of each handler instance.
	 * @param context the Context this handler belongs to
	 * @param settings the settings to use
	 */
	void init(Context context, ViewSettings settings);

	/**
	 * Invoked when the associated Context gets destroyed.
	 */
	void destroy();

	/**
	 * Adds a view root to this handler.
	 * <p>
	 * A handler often has to listen to bubbling events from view components that wish to be wired to the Context or from a ContextBuilder that
	 * wishes to know the parent Context.
	 * @param view the view root to add to the handler
	 */
	void addViewRoot(Container view);

	/**
	 * Removes a view root from this handler.
	 * <p>
	 * The handler should immediately stop to listen to bubbling events from children of the specified view root.
	 * @param view the view root to remove from the handler
	 */
	void removeViewRoot(Container view);

}
