package org.spicefactory.parsley.command;

/**
 * API for creating container managed commands programmatically.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class ManagedCommands {

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a builder for the specified command instance.
	 * @return a new builder for the specified command instance
	 */
	public static ManagedCommandBuilder wrap(Object command) {
		return new ManagedCommandBuilder(command);
	}

	/**
	 * Creates a builder for the specified command type.
	 * @return a new builder for the specified command type
	 */
	public static ManagedCommandBuilder create(Class<?> commandType) {
		return new ManagedCommandBuilder(commandType);
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////
}
