package org.spicefactory.parsley.command;

import javax.annotation.Nullable;

import org.spicefactory.parsley.command.config.CommandObserverAnnotationConfig;
import org.spicefactory.parsley.config.GuiceParsleyConfig;
import org.spicefactory.parsley.core.context.Context;
import org.spicefactory.parsley.swing.config.SwingParsleyConfig;

import com.google.inject.Guice;
import com.google.inject.Stage;
import com.google.inject.util.Modules;

public class SwingMapCommandDslTest extends MapCommandDslTest {

	@Override
	protected void configureCommandFlow() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void dispatchMessage(Object message, @Nullable String scope, Object selector) {
		try {
			super.dispatchMessage(message, scope, selector);
			Thread.sleep(100); // Let the time for swing command to execute.
		}
		catch (InterruptedException e) {
		}
	}

	@Override
	protected Context buildContext() {
		Context context =
				Guice.createInjector(Stage.DEVELOPMENT,
						Modules.override(new GuiceParsleyConfig(), new CommandObserverAnnotationConfig()).with(new SwingParsleyConfig()))
						.getInstance(Context.class);
		setContext(context);
		return context;
	}

}
