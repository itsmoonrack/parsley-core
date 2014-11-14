package org.spicefactory.parsley.command;

import javax.annotation.Nullable;

import org.spicefactory.lib.command.Command;
import org.spicefactory.lib.command.adapter.CommandAdapterFactory;
import org.spicefactory.lib.command.base.AbstractCommandExecutor;
import org.spicefactory.lib.command.events.CommandException;
import org.spicefactory.lib.command.lifecycle.CommandLifecycle;
import org.spicefactory.parsley.core.command.ManagedCommandProxy;
import org.spicefactory.parsley.core.context.Context;

public class DefaultManagedCommandProxy extends AbstractCommandExecutor implements ManagedCommandProxy {

	private String id;
	private Class<?> type;
	private Command target;
	private final Context context;

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a new instance.
	 * @param context the Context the command belongs to
	 */
	DefaultManagedCommandProxy(@Nullable Context context) {
		this(context, null, null);
	}

	/**
	 * Creates a new instance.
	 * @param context the Context the command belongs to
	 * @param target the target command to be executed by this proxy
	 */
	DefaultManagedCommandProxy(@Nullable Context context, @Nullable Command command) {
		this(context, command, null);
	}

	/**
	 * Creates a new instance.
	 * @param context the Context the command belongs to
	 * @param target the target command to be executed by this proxy
	 * @param id the id the command is registered with in the Context
	 */
	DefaultManagedCommandProxy(@Nullable Context context, @Nullable Command command, @Nullable String id) {
		this.id = id;
		this.target = command;
		this.context = context;
	}

	/**
	 * The target command that this proxy should execute.
	 * <p>
	 * The <code>type</code> and <code>target</code> properties are mutually exclusive.
	 * </p>
	 */
	void setTarget(Command target) {
		this.target = target;
		this.type = null;
	}

	/**
	 * The type of command that this proxy should execute.
	 * <p>
	 * The <code>type</code> and <code>target</code> properties are mutually exclusive.
	 * </p>
	 */
	void setType(Class<?> type) {
		this.type = type;
		this.target = null;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	@Override
	public Command getTarget() {
		return target;
	}

	@Override
	public String getID() {
		return id;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	@Override
	protected CommandLifecycle createLifecycle() {
		return new ManagedCommandLifecycle(context, this);
	}

	@Override
	protected void doExecute() {
		if (target == null) {
			try {
				CommandAdapterFactory factory = context.getInstance(CommandAdapterFactory.class);
				Object instance = lifecycle().createInstance(type, data);
				target = (instance instanceof Command) ? (Command) instance : factory.createAdapter(instance);
			}
			catch (Exception e) {
				error(new CommandException(this, target, e));
				return;
			}
		}
		executeCommand(target);
	}
}
