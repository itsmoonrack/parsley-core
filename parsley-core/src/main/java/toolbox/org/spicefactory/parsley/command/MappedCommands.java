package org.spicefactory.parsley.command;

import javax.inject.Provider;

/**
 * API for mapping commands to messages programmatically.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class MappedCommands {

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a new mapping builder for the specified command type.
	 * @return a new mapping builder for the specified command type
	 */
	public static MappedCommandBuilder create(Class<?> type) {
		return MappedCommandBuilder.forType(type);
	}

	/**
	 * Creates a new mapping builder for the specified provider.
	 * <p>
	 * The provider should not expect any arguments and create a new command instance on each invocation. It will get invoked for each matching
	 * message that triggers a command execution.
	 * </p>
	 * @param provider the provider for creating new command instances
	 * @param type the type of command the factory creates
	 */
	public static <T> MappedCommandBuilder provide(Provider<T> provider, Class<? extends T> type) {
		return MappedCommandBuilder.forProvider(provider, type);
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////
}
