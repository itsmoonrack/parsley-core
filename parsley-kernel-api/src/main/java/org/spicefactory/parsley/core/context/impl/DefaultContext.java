package org.spicefactory.parsley.core.context.impl;

import org.slf4j.Logger;
import org.spicefactory.parsley.core.context.Context;
import org.spicefactory.parsley.core.context.ContextListener;
import org.spicefactory.parsley.core.events.ContextEvent;
import org.spicefactory.parsley.core.events.EventDispatcher;

public final class DefaultContext extends EventDispatcher<ContextListener, ContextEvent> implements Context, ContextListener {

	@Inject
	private Logger logger;

	private final Context[] parents;

	public DefaultContext(Context[] parents) {
		this.parents = parents;

		addContextListener(this);
		for (Context p : parents) {
			p.addContextListener(l);
		}
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

	@Override
	public void addContextListener(ContextListener l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeContextListener(ContextListener l) {
		// TODO Auto-generated method stub

	}

	@Override
	public Context[] getParents() {
		return parents;
	}

}
