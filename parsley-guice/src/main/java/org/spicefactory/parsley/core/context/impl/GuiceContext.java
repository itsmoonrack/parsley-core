package org.spicefactory.parsley.core.context.impl;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.spicefactory.lib.event.AbstractEventDispatcher;
import org.spicefactory.parsley.command.MappedCommandBuilder;
import org.spicefactory.parsley.core.context.Context;
import org.spicefactory.parsley.core.context.ContextListener;
import org.spicefactory.parsley.core.events.ContextEvent;
import org.spicefactory.parsley.core.scope.ScopeManager;
import org.spicefactory.parsley.core.view.ViewManager;

import com.google.inject.Injector;

@Singleton
public class GuiceContext extends AbstractEventDispatcher<ContextListener, ContextEvent> implements Context {

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	@Inject
	Injector injector;

	@Inject
	ScopeManager scopeManager;

	@Inject
	void mapCommands(List<MappedCommandBuilder> mappedCommands) {
		for (MappedCommandBuilder aMappedCommand : mappedCommands) {
			aMappedCommand.register(this);
		}
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	@Override
	public void injectMembers(Object instance) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> T getInstance(Class<T> type) {
		return injector.getInstance(type);
	}

	@Override
	public Context[] getParents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ScopeManager getScopeManager() {
		return scopeManager;
	}

	@Override
	public ViewManager getViewManager() {
		return null;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////
}
