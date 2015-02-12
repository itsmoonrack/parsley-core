package org.spicefactory.parsley.command.impl;

import javax.annotation.Nullable;

import org.spicefactory.lib.command.Command;
import org.spicefactory.lib.command.lifecycle.CommandLifecycle;
import org.spicefactory.lib.command.proxy.DefaultCommandProxy;
import org.spicefactory.parsley.core.command.ManagedCommandProxy;
import org.spicefactory.parsley.core.context.Context;

public class DefaultManagedCommandProxy extends DefaultCommandProxy implements ManagedCommandProxy {

	private int id;
	private Context context;

	/////////////////////////////////////////////////////////////////////////////
	// Component exposed API.
	/////////////////////////////////////////////////////////////////////////////

	/**
	 * Creates a new instance.
	 */
	public DefaultManagedCommandProxy() {
		this(null, null, -1);
	}

	/**
	 * Creates a new instance.
	 * @param context the Context the command belongs to
	 */
	public DefaultManagedCommandProxy(@Nullable Context context) {
		this(context, null, -1);
	}

	/**
	 * Creates a new instance.
	 * @param context the Context the command belongs to
	 * @param target the target command to be executed by this proxy
	 */
	public DefaultManagedCommandProxy(@Nullable Context context, @Nullable Command command) {
		this(context, command, -1);
	}

	/**
	 * Creates a new instance.
	 * @param context the Context the command belongs to
	 * @param target the target command to be executed by this proxy
	 * @param id the id the command is registered with in the Context
	 */
	public DefaultManagedCommandProxy(@Nullable Context context, @Nullable Command command, int id) {
		this.id = id;
		setTarget(command);
		setContext(context);
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

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

}
