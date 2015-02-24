package org.spicefactory.parsley.messaging.config;

import org.spicefactory.parsley.messaging.model.EventSource;
import org.spicefactory.parsley.messaging.model.EventSourceAnnotation;
import org.spicefactory.parsley.messaging.model.MessageHandlers;
import org.spicefactory.parsley.messaging.model.MessageHandlersAnnotation;
import org.spicefactory.parsley.messaging.model.MessageInterceptors;
import org.spicefactory.parsley.messaging.model.MessageInterceptorsAnnotation;
import org.spicefactory.parsley.messaging.model.TestMessageHandlers;
import org.spicefactory.parsley.messaging.model.TestMessageHandlersAnnotation;

import com.google.inject.AbstractModule;

public class MessagingAnnotationConfig extends AbstractModule {

	@Override
	protected void configure() {
		bind(EventSource.class).to(EventSourceAnnotation.class);
		//		bind(FaultyMessageHandlers.class)
		bind(MessageHandlers.class).to(MessageHandlersAnnotation.class);
		bind(MessageInterceptors.class).to(MessageInterceptorsAnnotation.class);

		bind(TestMessageHandlers.class).to(TestMessageHandlersAnnotation.class);
	}

}
