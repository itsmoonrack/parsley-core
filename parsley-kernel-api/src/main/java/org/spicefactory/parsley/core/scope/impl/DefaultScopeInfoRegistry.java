package org.spicefactory.parsley.core.scope.impl;

import java.util.ArrayList;
import java.util.List;

import org.spicefactory.parsley.core.scope.ScopeDefinition;
import org.spicefactory.parsley.core.scope.ScopeInfo;
import org.spicefactory.parsley.core.scope.ScopeInfoRegistry;

@Singleton
public class DefaultScopeInfoRegistry implements ScopeInfoRegistry {

	public DefaultScopeInfoRegistry(List<ScopeDefinition> newScopes, List<ScopeInfo> parentScopes) {
		this.newScopes = newScopes;
		this.parentScopes = parentScopes;
		this.activeScopes = new ArrayList<ScopeInfo>();
	}

	@Override
	public List<ScopeDefinition> getNewScopes() {
		return newScopes;
	}

	@Override
	public List<ScopeInfo> getParentScopes() {
		return parentScopes;
	}

	@Override
	public List<ScopeInfo> getActiveScopes() {
		return activeScopes;
	}

	@Override
	public void addActiveScope(ScopeInfo info) {
		activeScopes.add(info);
	}

	private final List<ScopeDefinition> newScopes;
	private final List<ScopeInfo> parentScopes;
	private final List<ScopeInfo> activeScopes;

}
