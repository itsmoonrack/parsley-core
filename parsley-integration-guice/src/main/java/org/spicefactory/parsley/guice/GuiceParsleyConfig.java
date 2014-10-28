package org.spicefactory.parsley.guice;

import org.spicefactory.parsley.core.messaging.MessageSettings;
import org.spicefactory.parsley.core.messaging.impl.GuiceMessagingConfig;
import org.spicefactory.parsley.core.scope.Scope;
import org.spicefactory.parsley.core.scope.ScopeDefinition;
import org.spicefactory.parsley.core.scope.impl.GuiceScopeConfig;
import org.spicefactory.parsley.core.view.ViewSettings;
import org.spicefactory.parsley.guice.messaging.GuiceMessagingTypeListener;

import com.google.inject.AbstractModule;

@ViewSettings
@MessageSettings
@ScopeDefinition(name = Scope.GLOBAL)
public class GuiceParsleyConfig extends AbstractModule {

	@Override
	protected void configure() {
		// Registers type listener for MessageDispatcher instances. Do not bind listener
		// for swing components as the reflection on these classes can be heavy.
		// It is instead recommended to inject a presentation model in a view with
		// FastInject property and having a MessageDispatcher field in this model.
		bindListener(new NoComponentMatcher(), new GuiceMessagingTypeListener());

		// Binds infrastructure (framework) services.
		install(new GuiceMessagingConfig());
		install(new GuiceScopeConfig());
	}

}
