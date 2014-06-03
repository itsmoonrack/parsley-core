package org.spicefactory.parsley.core.view.impl;

import java.awt.Container;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.spicefactory.parsley.core.context.Context;
import org.spicefactory.parsley.core.context.ContextListener;
import org.spicefactory.parsley.core.events.ContextEvent;
import org.spicefactory.parsley.core.view.ViewManager;
import org.spicefactory.parsley.core.view.ViewRootHandler;
import org.spicefactory.parsley.core.view.ViewSettings;

/**
 * Default implementation of the ViewManager interface.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
@Singleton
@ViewSettings
// TODO: ViewSettings must be on Applet, as well as global settings/configuration.
public class DefaultViewManager implements ViewManager, ContainerListener, ContextListener {

	@Inject
	private Logger logger;

	private final Context context;

	private final ViewSettings settings;

	private final List<ViewRootHandler> handlers;

	private final Map<Container, ViewRoot> viewRoots = new IdentityHashMap<Container, DefaultViewManager.ViewRoot>();

	private final static Map<Container, ViewManager> viewRootRegistry = new IdentityHashMap<Container, ViewManager>();

	@Inject
	DefaultViewManager(Context c, @Nullable ViewSettings s) {
		context = c;
		context.addContextListener(this);

		if (s == null) {
			settings = this.getClass().getAnnotation(ViewSettings.class);
		} else {
			settings = s;
		}

		handlers = new ArrayList<ViewRootHandler>(settings.viewRootHandlers().length);

		for (Class<? extends ViewRootHandler> type : settings.viewRootHandlers()) {
			handlers.add(c.getInstance(type));
		}
	}

	@Override
	public void addViewRoot(Container view) {
		if (viewRoots.containsKey(view)) {
			return;
		}
		logger.trace("Adding view root {}/{}.", view.getName(), view.getClass());

		if (viewRootRegistry.containsKey(view)) {
			// Does not allows two view managers on the same view, but allows switching them.
			logger.trace("Switching ViewManager for view root '{}'.", view.getClass());
			ViewManager viewManager = viewRootRegistry.get(view);
			viewManager.removeViewRoot(view);
		}
		viewRootRegistry.put(view, this);

		boolean autoRemove = settings.autoRemoveViewRoots();
		if (autoRemove) {
			view.addContainerListener(this);
		} else {
			// TODO: implement if needed.
		}

		viewRoots.put(view, new ViewRoot(view, autoRemove));

		for (ViewRootHandler handler : handlers) {
			handler.addViewRoot(view);
		}
	}

	@Override
	public void removeViewRoot(Container view) {
		ViewRoot viewRoot = viewRoots.remove(view);
		if (viewRoot != null) {
			logger.trace("Removing view root {}/{}.", view.getName(), view.getClass());
			removeViewRoot(viewRoot);
			if (viewRoots.size() == 0 && settings.autoDestroyContext()) {
				logger.trace("Last view root removed from ViewManager. Destroying Context.");
				context.destroy();
			}
		}
	}

	private void removeViewRoot(ViewRoot viewRoot) {
		for (ViewRootHandler handler : handlers) {
			handler.removeViewRoot(viewRoot.view);
		}

		if (viewRoot.autoRemove) {
			viewRoot.view.removeContainerListener(this);
		} else {
			// TODO: implement if needed.
		}

		viewRootRegistry.remove(viewRoot);
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements ContainerListener.
	///////////////////////////////////////////////////////////////////////////

	@Override
	public void componentAdded(ContainerEvent e) {
		// Do nothing.
	}

	@Override
	public void componentRemoved(ContainerEvent e) {
		removeViewRoot(e.getContainer());
	}

	///////////////////////////////////////////////////////////////////////////
	// Implements ContextListener.
	///////////////////////////////////////////////////////////////////////////

	@Override
	public void contextDestroyed(ContextEvent e) {
		context.removeContextListener(this);
		for (ViewRoot viewRoot : viewRoots.values()) {
			removeViewRoot(viewRoot);
		}
		viewRoots.clear();
		for (ViewRootHandler handler : handlers) {
			handler.destroy();
		}
	}

	private class ViewRoot {
		public final Container view;
		public final boolean autoRemove;

		public ViewRoot(Container view, boolean autoRemove) {
			this.view = view;
			this.autoRemove = autoRemove;
		}
	}

}
