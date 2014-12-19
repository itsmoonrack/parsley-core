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

	private int id;
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
		this(context, null, -1);
	}

	/**
	 * Creates a new instance.
	 * @param context the Context the command belongs to
	 * @param target the target command to be executed by this proxy
	 */
	DefaultManagedCommandProxy(@Nullable Context context, @Nullable Command command) {
		this(context, command, -1);
	}

	/**
	 * Creates a new instance.
	 * @param context the Context the command belongs to
	 * @param target the target command to be executed by this proxy
	 * @param id the id the command is registered with in the Context
	 */
	DefaultManagedCommandProxy(@Nullable Context context, @Nullable Command command, int id) {
		this.id = id;
		this.target = command;
		this.context = context;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	/**
	 * The target command that this proxy should execute.
	 * <p>
	 * The <code>type</code> and <code>target</code> properties are mutually exclusive.
	 * </p>
	 */
	public void setTarget(Command value) {
		this.target = value;
		this.type = null;
	}

	@Override
	public Command getTarget() {
		return target;
	}

	/**
	 * The type of command that this proxy should execute.
	 * <p>
	 * The <code>type</code> and <code>target</code> properties are mutually exclusive.
	 * </p>
	 */
	public void setType(Class<?> value) {
		this.type = value;
		this.target = null;
	}

	@Override
	public int getID() {
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
		if (target == null && type == null) {
			throw new IllegalStateException("Either target or type property must be set.");
		}
		if (target == null) {
			try {
				CommandAdapterFactory factory = context.getInstance(CommandAdapterFactory.class);
				Object instance = lifecycle().createInstance(type, data);
				target = (instance instanceof Command) ? (Command) instance : factory.createAdapter(instance);
			}
			catch (Exception e) {
				exception(new CommandException(this, target, e));
				return;
			}
		}
		executeCommand(target);
	}
}
