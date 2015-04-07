package org.spicefactory.parsley.config;

import java.util.LinkedList;
import java.util.List;

import org.spicefactory.lib.command.adapter.CommandAdapters;
import org.spicefactory.lib.command.builder.CommandBuilder;
import org.spicefactory.lib.command.light.LightCommandAdapterFactory;
import org.spicefactory.parsley.command.MappedCommandBinder;
import org.spicefactory.parsley.command.MappedCommandBuilder;
import org.spicefactory.parsley.core.command.CommandManager;
import org.spicefactory.parsley.core.command.impl.DefaultCommandManager;
import org.spicefactory.parsley.core.context.Context;
import org.spicefactory.parsley.core.context.impl.DefaultContext;
import org.spicefactory.parsley.core.messaging.MessageReceiverRegistry;
import org.spicefactory.parsley.core.messaging.MessageRouter;
import org.spicefactory.parsley.core.messaging.impl.DefaultMessageReceiverRegistry;
import org.spicefactory.parsley.core.messaging.impl.DefaultMessageRouter;
import org.spicefactory.parsley.core.scope.impl.DefaultScopeConfig;
import org.spicefactory.parsley.core.view.ViewSettings;
import org.spicefactory.parsley.view.FastInject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

@ViewSettings
public final class GuiceParsleyConfig extends AbstractModule {

	private final GuiceParsleyTypeListener listener = new GuiceParsleyTypeListener();

	static {
		// Adds this factory to the registry of command adapters.
		// We consider that it is configuration code hence it is
		// acceptable to make use of static keyword in this case
		// so it remains in the JVM between application restarts.
		CommandAdapters.addFactory(new LightCommandAdapterFactory());
	}

	@Override
	protected void configure() {
		// Registers type listener for managed instances. Do not bind listener
		// for swing components as the reflection on these classes can be heavy.
		// It is instead recommended to inject a presentation model in a view with
		// FastInject property and having a MessageDispatcher field in this model.
		bindListener(new NoComponentMatcher(), listener);

		// Binds infrastructure (framework) services.
		bind(CommandManager.class).to(DefaultCommandManager.class);
		bind(Context.class).to(DefaultContext.class);
		bind(MessageRouter.class).to(DefaultMessageRouter.class);
		bind(MessageReceiverRegistry.class).to(DefaultMessageReceiverRegistry.class);

		// Installs infrastructure (framework) configuration.
		install(new DefaultScopeConfig());

		// Fast static injection for view classes.
		requestStaticInjection(FastInject.class);
	}

	private static final List<MappedCommandBuilder> mappedCommands = new LinkedList<MappedCommandBuilder>();

	@Provides
	protected List<MappedCommandBuilder> getMappedCommands() {
		return mappedCommands;
	}

	/**
	 * Binds a command to the framework, using just-in-time binding.
	 * <p>
	 * A command needs to be annotated by @MapCommand
	 * @param command
	 */
	public static void bindCommand(Class<?> command) {
		mappedCommands.add(MappedCommandBinder.mapCommand(command));
	}

	/**
	 * Binds a command factory to a message, using just-in-time binding.
	 */
	public static MappedCommandBuilder bindCommand(Class<?> messageType, CommandBuilder target) {
		MappedCommandBuilder builder = MappedCommandBinder.mapCommand(messageType, target);
		mappedCommands.add(builder);
		return builder;
	}
}
