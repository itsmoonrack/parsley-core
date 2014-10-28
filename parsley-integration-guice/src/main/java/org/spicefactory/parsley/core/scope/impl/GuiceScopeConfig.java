package org.spicefactory.parsley.core.scope.impl;

import java.util.LinkedList;
import java.util.List;

import org.spicefactory.parsley.core.command.CommandManager;
import org.spicefactory.parsley.core.scope.Scope;
import org.spicefactory.parsley.core.scope.ScopeDefinition;
import org.spicefactory.parsley.core.scope.ScopeInfo;
import org.spicefactory.parsley.core.scope.ScopeInfoRegistry;
import org.spicefactory.parsley.core.scope.ScopeManager;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

@ScopeDefinition(name = Scope.GLOBAL_)
public class GuiceScopeConfig extends AbstractModule {

	@Override
	protected void configure() {
		bind(ScopeInfoRegistry.class).to(DefaultScopeInfoRegistry.class);
		bind(ScopeManager.class).to(GuiceScopeManager.class);
	}

	@Provides
	protected List<ScopeDefinition> newScopes() {
		return new LinkedList<ScopeDefinition>();
	}

	@Provides
	protected List<ScopeInfo> parentScopes(CommandManager commandManager) {
		// TODO: Scopes are hard-coded here. Should create a scope per Module, Scope.LOCAL,
		// and possibility to add more. Also check the inherited property.
		List<ScopeInfo> parentScopes = new LinkedList<ScopeInfo>();
		parentScopes.add(new GuiceScopeInfo(Scope.GLOBAL, commandManager));
		return parentScopes;
	}

}
