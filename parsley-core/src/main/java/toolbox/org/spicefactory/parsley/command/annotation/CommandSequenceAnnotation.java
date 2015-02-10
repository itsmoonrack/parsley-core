package org.spicefactory.parsley.command.annotation;

import org.spicefactory.lib.command.group.CommandSequence;

/**
 * Annotation for command sequences declared in FXML or XML configuration.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class CommandSequenceAnnotation extends AbstractCommandGroupAnnotation {

	public CommandSequenceAnnotation() {
		super(CommandSequence.class);
	}

}
