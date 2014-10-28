package org.spicefactory.parsley.core.scope.impl;

import java.util.ArrayList;
import java.util.List;

import org.spicefactory.parsley.core.scope.Scope;
import org.spicefactory.parsley.core.scope.ScopeDefinition;
import org.spicefactory.parsley.core.scope.ScopeInfo;
import org.spicefactory.parsley.core.scope.ScopeInfoRegistry;
import org.spicefactory.parsley.core.scope.ScopeManager;

import com.google.inject.AbstractModule;

@ScopeDefinition(name = Scope.GLOBAL)
public class GuiceScopeConfig extends AbstractModule {

	@Override
	protected void configure() {
		// TODO: Scopes are hard-coded here. Should create a scope per Module, Scope.LOCAL,
		// and possibility to add more. Also check the inherited property.
		List<ScopeDefinition> newScopes = new ArrayList<ScopeDefinition>();
		List<ScopeInfo> parentScopes = new ArrayList<ScopeInfo>();
		parentScopes.add(new DefaultScopeInfo(getClass().getAnnotation(ScopeDefinition.class)));

		bind(ScopeInfoRegistry.class).toInstance(new DefaultScopeInfoRegistry(newScopes, parentScopes));
		bind(ScopeManager.class).to(GuiceScopeManager.class);
	}
}
