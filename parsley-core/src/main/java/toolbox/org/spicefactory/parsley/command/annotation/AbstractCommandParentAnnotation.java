package org.spicefactory.parsley.command.annotation;

import java.util.LinkedList;
import java.util.List;

import org.spicefactory.parsley.core.command.ManagedCommandFactory;
import org.spicefactory.parsley.core.context.Context;

/**
 * Base class for any kind of annotation that accepts a List of command annotations as children.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public abstract class AbstractCommandParentAnnotation extends AbstractCommandAnnotation {

	/**
	 * The commands to be added to this command group or flow.
	 */
	public final List<NestedCommandAnnotation> commands = new LinkedList<NestedCommandAnnotation>();

	public abstract ManagedCommandFactory resolve(Context context);

}
