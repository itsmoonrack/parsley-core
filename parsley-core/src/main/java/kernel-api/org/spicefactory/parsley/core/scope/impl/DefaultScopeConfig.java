package org.spicefactory.parsley.core.scope.impl;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Singleton;

import org.spicefactory.parsley.core.command.CommandManager;
import org.spicefactory.parsley.core.messaging.MessageRouter;
import org.spicefactory.parsley.core.messaging.MessageSettings;
import org.spicefactory.parsley.core.scope.Scope;
import org.spicefactory.parsley.core.scope.ScopeDefinition;
import org.spicefactory.parsley.core.scope.ScopeInfo;
import org.spicefactory.parsley.core.scope.ScopeInfoRegistry;
import org.spicefactory.parsley.core.scope.ScopeManager;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

@MessageSettings
@ScopeDefinition(name = Scope.GLOBAL)
public class DefaultScopeConfig extends AbstractModule {

	@Override
	protected void configure() {
		bind(ScopeInfoRegistry.class).to(DefaultScopeInfoRegistry.class);
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
		parentScopes.add(new DefaultScopeInfo(DefaultScopeConfig.class.getAnnotation(ScopeDefinition.class), commandManager));
		return parentScopes;
	}

	@Provides
	@Singleton
	protected ScopeManager scopeManager(ScopeInfoRegistry scopeInfoRegistry, MessageRouter messageRouter, CommandManager commandManager) {
		// TODO: MessageSettings are hard-coded here. Should use a settings per Module
		// and inherits from parent if not found.
		return new DefaultScopeManager(scopeInfoRegistry, messageRouter, commandManager,
				DefaultScopeConfig.class.getAnnotation(MessageSettings.class));
	}

}
