package org.spicefactory.parsley.command;

import org.junit.Before;
import org.spicefactory.lib.command.Command;
import org.spicefactory.lib.command.builder.Commands;
import org.spicefactory.parsley.command.config.CommandObserverAnnotationConfig;
import org.spicefactory.parsley.command.target.AsyncCommand;
import org.spicefactory.parsley.config.GuiceParsleyConfig;
import org.spicefactory.parsley.core.context.Context;

import com.google.inject.Guice;
import com.google.inject.Stage;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class CommandFactoryDslTest extends CommandFactoryTestBase {

	@Before
	public void createContext() {
		context = buildContext();
	}

	@Override
	protected void configureSingleCommand() {
		configure(ManagedCommands.create(AsyncCommand.class));
	}

	@Override
	protected void configureCommandSequence() {
		configure(ManagedCommands.wrap(createCommandSequence()));
	}

	private Command createCommandSequence() {
		return Commands.asSequence() //
				.add(new AsyncCommand("1")) //
				.add(new AsyncCommand("2")) //
				.build();
	}

	@Override
	protected void configureParallelCommands() {
		configure(ManagedCommands.wrap(createCommandParallel()));
	}

	private Command createCommandParallel() {
		return Commands.inParallel() //
				.add(new AsyncCommand("1")) //
				.add(new AsyncCommand("2")) //
				.build();
	}

	@Override
	protected void configureCommandFlow() {
		// TODO Auto-generated method stub

	}

	private void configure(ManagedCommandBuilder builder) {
		setProxy(builder.build(context));
	}

	private Context buildContext() {
		//		Context context = ContextBuilder //
		//				.newBuilder() //
		//				.config(processor) //
		//				.build();
		Context context =
				Guice.createInjector(Stage.DEVELOPMENT, new GuiceParsleyConfig(), new CommandObserverAnnotationConfig()).getInstance(
						Context.class);
		setContext(context);
		return context;
	}

}
