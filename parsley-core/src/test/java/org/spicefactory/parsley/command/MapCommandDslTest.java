package org.spicefactory.parsley.command;

import javax.inject.Provider;

import org.spicefactory.lib.command.Command;
import org.spicefactory.lib.command.builder.Commands;
import org.spicefactory.lib.command.group.CommandParallel;
import org.spicefactory.lib.command.group.CommandSequence;
import org.spicefactory.parsley.command.config.CommandObserverAnnotationConfig;
import org.spicefactory.parsley.command.target.AsyncCommandWithTrigger;
import org.spicefactory.parsley.command.target.SyncCommand;
import org.spicefactory.parsley.command.trigger.TriggerA;
import org.spicefactory.parsley.config.GuiceParsleyConfig;
import org.spicefactory.parsley.core.context.Context;
import org.spicefactory.parsley.core.scope.Scope;

import com.google.inject.Guice;
import com.google.inject.Stage;

public class MapCommandDslTest extends MapCommandTestBase {

	@Override
	protected void configureSingleCommand() {
		Context context = buildContext();
		MappedCommands.create(AsyncCommandWithTrigger.class).register(context);
	}

	@Override
	protected void configureSelector() {
		Context context = buildContext();
		MappedCommands.create(AsyncCommandWithTrigger.class).selector("selector").register(context);
	}

	@Override
	protected void configureLocalScope() {
		Context context = buildContext();
		MappedCommands.create(AsyncCommandWithTrigger.class).scope(Scope.LOCAL).register(context);
	}

	@Override
	protected void configureOrder() {
		Context context = buildContext();
		MappedCommands //
				.provide(new Provider<SyncCommand>() {
					@Override
					public SyncCommand get() {
						return new SyncCommand("2");
					}
				}, SyncCommand.class) //
				.messageType(TriggerA.class) //
				.order(2) //
				.register(context);
		MappedCommands //
				.provide(new Provider<SyncCommand>() {
					@Override
					public SyncCommand get() {
						return new SyncCommand("1");
					}
				}, SyncCommand.class) //
				.messageType(TriggerA.class) //
				.order(1) //
				.register(context);
	}

	@Override
	protected void configureCommandSequence() {
		Context context = buildContext();
		MappedCommands //
				.provide(createCommandSequence(), CommandSequence.class) //
				.messageType(TriggerA.class) //
				.register(context);
	}

	private Provider<Command> createCommandSequence() {
		return new Provider<Command>() {
			@Override
			public Command get() {
				return Commands.asSequence() //
				.add(new AsyncCommandWithTrigger("1")) //
				.add(new AsyncCommandWithTrigger("2")) //
				.build();
			}
		};
	}

	@Override
	protected void configureParallelCommands() {
		Context context = buildContext();
		MappedCommands //
				.provide(createParallelCommands(), CommandParallel.class) //
				.messageType(TriggerA.class) //
				.register(context);
	}

	private Provider<Command> createParallelCommands() {
		return new Provider<Command>() {
			@Override
			public Command get() {
				return Commands.inParallel() //
						.add(new AsyncCommandWithTrigger("1")) //
						.add(new AsyncCommandWithTrigger("2")) //
						.build();
			}
		};
	}

	@Override
	protected void configureCommandFlow() {
		// TODO Auto-generated method stub

	}

	protected Context buildContext() {
		Context context =
				Guice.createInjector(Stage.DEVELOPMENT, new GuiceParsleyConfig(), new CommandObserverAnnotationConfig()).getInstance(Context.class);
		setContext(context);
		return context;
	}

}
