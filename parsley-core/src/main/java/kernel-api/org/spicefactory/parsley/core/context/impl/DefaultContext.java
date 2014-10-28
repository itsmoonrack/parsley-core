package org.spicefactory.parsley.core.context.impl;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.spicefactory.parsley.core.context.Context;
import org.spicefactory.parsley.core.context.ContextListener;
import org.spicefactory.parsley.core.events.ContextEvent;
import org.spicefactory.parsley.core.events.AbstractEventDispatcher;
import org.spicefactory.parsley.core.registry.Registry;
import org.spicefactory.parsley.core.view.ViewManager;

public final class DefaultContext extends AbstractEventDispatcher<ContextListener, ContextEvent> implements Context, ContextListener {

	@Inject
	private Logger logger;

	private Registry registry;

	private final Context[] parents;
	private final ViewManager viewManager;

	@Inject
	public DefaultContext(ViewManager viewManager, Context[] parents) {
		this.parents = parents;
		this.viewManager = viewManager;

		addContextListener(this);
		for (Context p : parents) {
			p.addContextListener(new ParentContextListener());
		}
	}

	public void setRegistry(Registry registry) {
		this.registry = registry;
	}

	@Override
	protected void processEvent(ContextListener l, ContextEvent e) {
		switch (e.getId()) {
			case ContextEvent.DESTROYED:
				l.contextDestroyed(e);
				break;

			default:
				break;
		}

	}

	@Override
	public <T> T getInstance(Class<T> type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void contextDestroyed(ContextEvent e) {
		// TODO Auto-generated method stub

	}

	private class ParentContextListener implements ContextListener {

		@Override
		public void contextDestroyed(ContextEvent e) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public void addContextListener(ContextListener l) {
		addEventListener(l);
	}

	@Override
	public void removeContextListener(ContextListener l) {
		removeEventListener(l);
	}

	@Override
	public Context[] getParents() {
		return parents;
	}

	@Override
	public ViewManager getViewManager() {
		return viewManager;
	}

}
