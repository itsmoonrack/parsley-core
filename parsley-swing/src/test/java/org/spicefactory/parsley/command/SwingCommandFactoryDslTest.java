package org.spicefactory.parsley.command;

import org.junit.Before;
import org.spicefactory.parsley.command.config.CommandObserverAnnotationConfig;
import org.spicefactory.parsley.config.GuiceParsleyConfig;
import org.spicefactory.parsley.core.context.Context;
import org.spicefactory.parsley.swing.config.SwingParsleyConfig;

import com.google.inject.Guice;
import com.google.inject.Stage;
import com.google.inject.util.Modules;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class SwingCommandFactoryDslTest extends CommandFactoryDslTest {

	@Before
	@Override
	public void createContext() {
		context = buildContext();
	}

	@Override
	protected void execute() {
		super.execute();
		try {
			Thread.sleep(100); // Let the time for swing command to execute.
		}
		catch (InterruptedException e) {
		}
	}

	private Context buildContext() {
		//		Context context = ContextBuilder //
		//				.newBuilder() //
		//				.config(processor) //
		//				.build();
		Context context =
				Guice.createInjector(Stage.DEVELOPMENT,
						Modules.override(new GuiceParsleyConfig(), new CommandObserverAnnotationConfig()).with(new SwingParsleyConfig()))
						.getInstance(Context.class);
		setContext(context);
		return context;
	}

}
