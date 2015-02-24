package org.spicefactory.parsley.messaging;

import org.spicefactory.parsley.config.GuiceParsleyConfig;
import org.spicefactory.parsley.core.context.Context;

import com.google.inject.Guice;

public class MessagingAnnotationTest extends MessagingTestBase {

	@Override
	protected Context messagingContext() {
		return Guice.createInjector(new GuiceParsleyConfig()).getInstance(Context.class);
	}

}
