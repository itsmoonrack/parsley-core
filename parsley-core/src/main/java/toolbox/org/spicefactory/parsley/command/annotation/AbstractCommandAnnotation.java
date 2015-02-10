package org.spicefactory.parsley.command.annotation;

/**
 * Base annotation for commands declared in FXML or XML configuration.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public abstract class AbstractCommandAnnotation {

	/**
	 * The id of the command.
	 * <p>
	 * May be used to identify a command in a flow declared in XML (not necessary in FXML) or for matching CommandResult handlers.
	 */
	public int id;

}
