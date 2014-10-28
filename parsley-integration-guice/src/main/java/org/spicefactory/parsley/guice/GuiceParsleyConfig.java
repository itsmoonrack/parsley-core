package org.spicefactory.parsley.guice;

import org.spicefactory.parsley.core.command.impl.GuiceCommandConfig;
import org.spicefactory.parsley.core.messaging.MessageSettings;
import org.spicefactory.parsley.core.messaging.impl.GuiceMessagingConfig;
import org.spicefactory.parsley.core.scope.Scope;
import org.spicefactory.parsley.core.scope.ScopeDefinition;
import org.spicefactory.parsley.core.scope.impl.GuiceScopeConfig;
import org.spicefactory.parsley.core.view.ViewSettings;
import org.spicefactory.parsley.view.FastInject;

import com.google.inject.AbstractModule;

@ViewSettings
@MessageSettings
@ScopeDefinition(name = Scope.GLOBAL_)
public class GuiceParsleyConfig extends AbstractModule {

	@Override
	protected void configure() {
		// Registers type listener for MessageDispatcher instances. Do not bind listener
		// for swing components as the reflection on these classes can be heavy.
		// It is instead recommended to inject a presentation model in a view with
		// FastInject property and having a MessageDispatcher field in this model.
		bindListener(new NoComponentMatcher(), new GuiceParsleyTypeListener());

		// Binds infrastructure (framework) services.
		install(new GuiceCommandConfig());
		install(new GuiceMessagingConfig());
		install(new GuiceScopeConfig());

		// Fast static injection for view classes.
		requestStaticInjection(FastInject.class);
	}

}
