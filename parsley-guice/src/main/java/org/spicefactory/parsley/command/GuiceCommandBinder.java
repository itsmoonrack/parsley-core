package org.spicefactory.parsley.command;

import javax.inject.Provider;

import org.spicefactory.lib.command.builder.CommandBuilder;
import org.spicefactory.parsley.command.annotation.MapCommand;
import org.spicefactory.parsley.core.command.ManagedCommandFactory;
import org.spicefactory.parsley.core.command.ManagedCommandProxy;
import org.spicefactory.parsley.core.context.Context;

public class GuiceCommandBinder {

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	/**
	 * Binds a command to the framework, using just-in-time binding.
	 * <p>
	 * A command needs to be annotated by @MapCommand
	 * @param command
	 */
	public static MappedCommandBuilder mapCommand(Class<?> command) {
		MapCommand mapCommand = command.getAnnotation(MapCommand.class);
		if (mapCommand == null) {
			throw new RuntimeException("Cannot map command '" + command.getSimpleName() + "'. @MapCommand annotation is missing.");
		}
		return MappedCommandBuilder.forType(command). //
				messageType(mapCommand.messageType()). //
				selector(mapCommand.selector()). //
				order(mapCommand.order()). //
				scope(mapCommand.scope());
	}

	/**
	 * Binds a command factory to a message, using just-in-time binding.
	 */
	public static MappedCommandBuilder mapCommand(Class<?> messageType, CommandBuilder target, Provider<Context> provider) {
		MapCommand mapCommand = DummyCommand.class.getAnnotation(MapCommand.class);
		return MappedCommandBuilder.forFactory(new Factory(provider, ManagedCommands.wrap(target))). //
				messageType(messageType). //
				selector(mapCommand.selector()). //
				order(mapCommand.order()). //
				scope(mapCommand.scope());
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	private static class Factory implements ManagedCommandFactory {

		private final Provider<Context> provider;
		private final ManagedCommandBuilder builder;

		public Factory(Provider<Context> provider, ManagedCommandBuilder builder) {
			this.provider = provider;
			this.builder = builder;
		}

		@Override
		public Class<?> type() {
			return null;
		}

		@Override
		public ManagedCommandProxy newInstance() {
			return builder.build(provider.get());
		}

	}

	@MapCommand
	private static class DummyCommand {
		// Used to get annotation defaults.
	}
}
