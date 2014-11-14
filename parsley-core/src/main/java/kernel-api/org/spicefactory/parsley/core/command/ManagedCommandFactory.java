package org.spicefactory.parsley.core.command;


/**
 * Factory responsible for creating instances of command proxies that are managed by the container.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface ManagedCommandFactory {

	/**
	 * The type of the command this factory produces.
	 */
	Class<?> type();

	/**
	 * Creates a new command proxy instance.
	 * @return a new command proxy instance
	 */
	ManagedCommandProxy newInstance();

}
