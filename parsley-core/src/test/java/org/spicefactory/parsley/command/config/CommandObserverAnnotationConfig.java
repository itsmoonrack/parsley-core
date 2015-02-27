package org.spicefactory.parsley.command.config;

import org.spicefactory.parsley.command.observer.CommandObservers;
import org.spicefactory.parsley.command.observer.CommandObserversAnnotation;
import org.spicefactory.parsley.command.observer.CommandStatusFlagAnnotation;
import org.spicefactory.parsley.command.observer.CommandStatusFlags;

import com.google.inject.AbstractModule;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class CommandObserverAnnotationConfig extends AbstractModule {

	@Override
	protected void configure() {
		bind(CommandObservers.class).to(CommandObserversAnnotation.class);
		bind(CommandStatusFlags.class).to(CommandStatusFlagAnnotation.class);
	}

}
