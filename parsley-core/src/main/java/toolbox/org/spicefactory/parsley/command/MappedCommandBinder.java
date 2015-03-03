package org.spicefactory.parsley.command;

import javax.inject.Provider;

import org.spicefactory.lib.command.builder.CommandBuilder;
import org.spicefactory.parsley.command.annotation.MappedCommand;

public class MappedCommandBinder {

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
		MappedCommand mapCommand = command.getAnnotation(MappedCommand.class);
		if (mapCommand == null) {
			throw new RuntimeException("Cannot map command '" + command.getSimpleName() + "'. @MapCommand annotation is missing.");
		}
		return MappedCommandBuilder.forType(command) //
				.messageType(mapCommand.value()) //
				.selector(mapCommand.selector()) //
				.order(mapCommand.order()) //
				.scope(mapCommand.scope());
	}

	/**
	 * Binds a command factory to a message, using just-in-time binding.
	 */
	public static MappedCommandBuilder mapCommand(Class<?> messageType, CommandBuilder target) {
		MappedCommand mapCommand = DummyCommand.class.getAnnotation(MappedCommand.class);
		return mapCommand(messageType, target, mapCommand.scope(), mapCommand.selector(), mapCommand.order());
	}

	/**
	 * Binds a command factory to a message, using just-in-time binding.
	 */
	public static MappedCommandBuilder mapCommand(Class<?> messageType, CommandBuilder target, String scope) {
		MappedCommand mapCommand = DummyCommand.class.getAnnotation(MappedCommand.class);
		return mapCommand(messageType, target, scope, mapCommand.selector(), mapCommand.order());
	}

	/**
	 * Binds a command factory to a message, using just-in-time binding.
	 */
	public static MappedCommandBuilder mapCommand(Class<?> messageType, CommandBuilder target, String scope, Object selector) {
		MappedCommand mapCommand = DummyCommand.class.getAnnotation(MappedCommand.class);
		return mapCommand(messageType, target, scope, selector, mapCommand.order());
	}

	/**
	 * Binds a command factory to a message, using just-in-time binding.
	 */
	public static MappedCommandBuilder mapCommand(Class<?> messageType, final CommandBuilder target, String scope, Object selector, int order) {
		return MappedCommandBuilder.forProvider(new Provider<CommandBuilder>() {
			@Override
			public CommandBuilder get() {
				return target;
			}
		}, CommandBuilder.class) //
				.messageType(messageType) //
				.selector(selector) //
				.order(order) //
				.scope(scope);
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	@MappedCommand
	private static class DummyCommand {
		// Used to get annotation defaults.
	}
}
