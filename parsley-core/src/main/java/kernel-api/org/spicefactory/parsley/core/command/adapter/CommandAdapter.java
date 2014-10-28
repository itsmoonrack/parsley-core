package org.spicefactory.parsley.core.command.adapter;

/**
 * Represents an adapter for a command type that does not implement one of the command interfaces.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public interface CommandAdapter {

	/**
	 * The target executed by this adapter.
	 */
	Object getTarget();

}
