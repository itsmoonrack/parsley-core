package org.spicefactory.parsley.command.annotation;

import org.spicefactory.parsley.core.command.ManagedCommandFactory;
import org.spicefactory.parsley.core.context.Context;

/**
 * Represents an annotation that produces a command and can be nested inside other command annotations.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public interface NestedCommandAnnotation extends CommandConfiguration {

	/**
	 * Creates a new command factory based on the configuration of this annotation.
	 * @param context the context this annotation is associated with
	 * @return a new command factory based on the configuration of this annotation
	 */
	ManagedCommandFactory resolve(Context context);

}
