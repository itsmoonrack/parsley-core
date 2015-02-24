package org.spicefactory.parsley.messaging;

import org.spicefactory.parsley.config.GuiceParsleyConfig;
import org.spicefactory.parsley.core.context.Context;
import org.spicefactory.parsley.messaging.config.MessagingAnnotationConfig;

import com.google.inject.Guice;
import com.google.inject.Stage;

public class LazyMessagingAnnotationTest extends MessagingTestBase {

	public LazyMessagingAnnotationTest() {
		// Guice lazily creates singleton in Development stage, and
		// up-front in Production stage. Hence we test Development.
		super(true);
	}

	@Override
	protected Context messagingContext() {
		return Guice.createInjector(Stage.DEVELOPMENT, new GuiceParsleyConfig(), new MessagingAnnotationConfig()).getInstance(Context.class);
	}

}
