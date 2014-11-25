package org.spicefactory.parsley;

import java.util.LinkedList;
import java.util.List;

import org.spicefactory.parsley.command.MappedCommandBuilder;
import org.spicefactory.parsley.core.command.CommandManager;
import org.spicefactory.parsley.core.command.impl.DefaultCommandManager;
import org.spicefactory.parsley.core.context.Context;
import org.spicefactory.parsley.core.context.impl.GuiceContext;
import org.spicefactory.parsley.core.messaging.MessageReceiverRegistry;
import org.spicefactory.parsley.core.messaging.MessageSettings;
import org.spicefactory.parsley.core.messaging.impl.DefaultMessageReceiverRegistry;
import org.spicefactory.parsley.core.scope.Scope;
import org.spicefactory.parsley.core.scope.ScopeDefinition;
import org.spicefactory.parsley.core.scope.ScopeInfo;
import org.spicefactory.parsley.core.scope.ScopeInfoRegistry;
import org.spicefactory.parsley.core.scope.ScopeManager;
import org.spicefactory.parsley.core.scope.impl.DefaultScopeInfoRegistry;
import org.spicefactory.parsley.core.scope.impl.GuiceScopeInfo;
import org.spicefactory.parsley.core.scope.impl.GuiceScopeManager;
import org.spicefactory.parsley.core.view.ViewSettings;
import org.spicefactory.parsley.view.FastInject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

@ViewSettings
@MessageSettings
@ScopeDefinition(name = Scope.GLOBAL_)
public class GuiceParsleyConfig extends AbstractModule {

	private final GuiceParsleyTypeListener listener = new GuiceParsleyTypeListener();

	@Override
	protected void configure() {
		// Registers type listener for MessageDispatcher instances. Do not bind listener
		// for swing components as the reflection on these classes can be heavy.
		// It is instead recommended to inject a presentation model in a view with
		// FastInject property and having a MessageDispatcher field in this model.
		bindListener(new NoComponentMatcher(), listener);

		// Binds infrastructure (framework) services.
		bind(CommandManager.class).to(DefaultCommandManager.class);
		bind(Context.class).to(GuiceContext.class);
		bind(MessageReceiverRegistry.class).to(DefaultMessageReceiverRegistry.class);
		bind(ScopeInfoRegistry.class).to(DefaultScopeInfoRegistry.class);
		bind(ScopeManager.class).to(GuiceScopeManager.class);

		// Fast static injection for view classes.
		requestStaticInjection(FastInject.class);
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

	@Provides
	protected List<MappedCommandBuilder> getMappedCommands() {
		return listener.mappedCommands;
	}
}