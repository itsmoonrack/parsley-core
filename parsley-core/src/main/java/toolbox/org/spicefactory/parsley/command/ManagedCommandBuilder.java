package org.spicefactory.parsley.command;

import org.spicefactory.lib.command.builder.CommandProxyBuilder;
import org.spicefactory.lib.command.events.CommandEvent;
import org.spicefactory.lib.event.EventListener;
import org.spicefactory.parsley.core.command.ManagedCommandProxy;
import org.spicefactory.parsley.core.context.Context;

/**
 * A builder DSL for creating command instances that are managed by the container.
 * <p>
 * This API adds on top of the stand-alone Spicelib Commands builder APIs the ability to let commands get managed by a Parsley Context during
 * execution.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class ManagedCommandBuilder {

	private int id;
	private final CommandProxyBuilder builder;
	private final DefaultManagedCommandProxy proxy;

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	ManagedCommandBuilder(Object target) {
		this.proxy = new DefaultManagedCommandProxy();
		this.builder = new CommandProxyBuilder(target, proxy);
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	/**
	 * The identifier of the managed command.
	 * <p>
	 * The id is optional and can be used for identifying matching command observers.
	 * </p>
	 * @param the identifier of the managed command
	 * @return this builder instance for method chaining
	 */
	public ManagedCommandBuilder id(int value) {
		id = value;
		return this;
	}

	/**
	 * Sets the timeout for the command execution.
	 * <p>
	 * When the specified amount of time is elapsed the command will abort with an error.
	 * </p>
	 * @param milliseconds the timeout for the command execution in milliseconds
	 * @return this builder instance for method chaining
	 */
	public ManagedCommandBuilder timeout(long milliseconds) {
		builder.timeout(milliseconds);
		return this;
	}

	/**
	 * Adds a value that can get passed to the command executed by the proxy this builder creates.
	 * @param value the value to pass to the command
	 * @return this builder instance for method chaining
	 */
	public ManagedCommandBuilder data(Object value) {
		builder.data(value);
		return this;
	}

	/**
	 * Adds a callback to invoke when the command completes successfully.
	 * <p>
	 * The result produced by the command will get passed to the callback.
	 * </p>
	 * @param callback the callback to invoke when the command completes successfully
	 * @return this builder instance for method chaining
	 */
	public ManagedCommandBuilder result(EventListener<CommandEvent> callback) {
		builder.result(callback);
		return this;
	}

	/**
	 * Adds a callback to invoke when the command raised an exception.
	 * <p>
	 * The cause of the exception will get passed to the callback.
	 * </p>
	 * @param callback the callback to invoke when the command produced an error
	 * @return this builder instance for method chaining
	 */
	public ManagedCommandBuilder exception(EventListener<CommandEvent> callback) {
		builder.exception(callback);
		return this;
	}

	/**
	 * Adds a callback to invoke when the command gets cancelled.
	 * <p>
	 * The callback should not expect any parameters.
	 * </p>
	 * @param callback the callback to invoke when the command gets cancelled
	 * @return this builder instance for method chaining
	 */
	public ManagedCommandBuilder cancel(EventListener<CommandEvent> callback) {
		builder.cancel(callback);
		return this;
	}

	/**
	 * Builds the target command without executing it, applying all configurations specified through this builder instance and associating it
	 * with the specified Context.
	 * @param the Context that is responsible for managing the command created by this builder
	 * @return the command proxy will all configuration of this builder applied
	 */
	public ManagedCommandProxy build(Context context) {
		proxy.setId(id);
		proxy.setContext(context);
		builder.build();
		return proxy;
	}

	/**
	 * Builds and executes the target command.
	 * <p>
	 * A shortcut for calling <code>build().execute()</code>. In case of asynchronous commands the returned proxy will still be active. In case
	 * of synchronous commands it will already be completed, so that adding event listeners won't have any effect.
	 * </p>
	 * @param the Context that is responsible for managing the command created by this builder
	 * @return the command that was built and executed by this builder
	 */
	public ManagedCommandProxy execute(Context context) {
		ManagedCommandProxy command = build(context);
		command.execute();
		return command;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////
}
