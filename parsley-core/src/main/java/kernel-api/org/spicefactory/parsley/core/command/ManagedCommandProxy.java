package org.spicefactory.parsley.core.command;

import org.spicefactory.lib.command.proxy.CommandProxy;

/**
 * Represents a single command proxy managed by the container.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface ManagedCommandProxy extends CommandProxy {

	/**
	 * The id the command is registered with in the Context.
	 */
	int getID();
}
