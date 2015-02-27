package org.spicefactory.parsley.command;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;

import javax.inject.Provider;

import org.junit.Test;
import org.spicefactory.lib.command.Command;
import org.spicefactory.lib.command.builder.Commands;
import org.spicefactory.lib.command.group.CommandSequence;
import org.spicefactory.parsley.command.observer.CommandObserversImmediate;
import org.spicefactory.parsley.command.observer.CommandObserversNonImmediate;
import org.spicefactory.parsley.command.target.SimpleCommand;
import org.spicefactory.parsley.command.trigger.Trigger;
import org.spicefactory.parsley.command.trigger.TriggerA;
import org.spicefactory.parsley.config.GuiceParsleyConfig;
import org.spicefactory.parsley.core.context.Context;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.Stage;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class CommandResultTest {

	private Context context;

	@Test
	public void testImmediate() {
		// Given
		final CommandObserversImmediate observers = new CommandObserversImmediate();
		// TODO: Make possible the one-liner bellow.
		//		context = ContextBuilder.newBuilder().object(observers).build();
		context = Guice.createInjector(Stage.DEVELOPMENT, new Module() {
			@Override
			public void configure(Binder binder) {
				binder.bind(CommandObserversImmediate.class).toInstance(observers);
			}
		}, new GuiceParsleyConfig()).getInstance(Context.class);

		MappedCommands //
				.provide(createSequence(), CommandSequence.class) //
				.messageType(Trigger.class) //
				.register(context);

		// When
		dispatchMessage();

		// Then
		assertThat(observers.firstResult, is(false));
		assertThat(observers.secondResult, is(false));
		assertThat(observers.allResults, is(false));

		// When
		SimpleCommand.activeCommand.complete("foo");

		// Then
		assertThat(observers.firstResult, is(true));
		assertThat(observers.secondResult, is(false));
		assertThat(observers.allResults, is(false));

		// When
		SimpleCommand.activeCommand.complete(new Date());

		// Then
		assertThat(observers.firstResult, is(true));
		assertThat(observers.secondResult, is(true));
		assertThat(observers.allResults, is(true));
	}

	@Test
	public void testNotImmediate() {
		// Given
		final CommandObserversNonImmediate observers = new CommandObserversNonImmediate();
		// TODO: Make possible the one-liner bellow.
		//		context = ContextBuilder.newBuilder().object(observers).build();
		context = Guice.createInjector(Stage.DEVELOPMENT, new Module() {
			@Override
			public void configure(Binder binder) {
				binder.bind(CommandObserversNonImmediate.class).toInstance(observers);
			}
		}, new GuiceParsleyConfig()).getInstance(Context.class);

		MappedCommands //
				.provide(createSequence(), CommandSequence.class) //
				.messageType(Trigger.class) //
				.register(context);

		// When
		dispatchMessage();

		// Then
		assertThat(observers.firstResult, is(false));
		assertThat(observers.secondResult, is(false));
		assertThat(observers.allResults, is(false));

		// When
		SimpleCommand.activeCommand.complete("foo");

		// Then
		assertThat(observers.firstResult, is(false));
		assertThat(observers.secondResult, is(false));
		assertThat(observers.allResults, is(false));

		// When
		SimpleCommand.activeCommand.complete(new Date());

		// Then
		assertThat(observers.firstResult, is(true));
		assertThat(observers.secondResult, is(true));
		assertThat(observers.allResults, is(true));
	}

	private Provider<Command> createSequence() {
		return new Provider<Command>() {
			@Override
			public Command get() {
				return Commands.asSequence() //
						.create(SimpleCommand.class) //
						.create(SimpleCommand.class) //
						.build();
			}
		};
	}

	private void dispatchMessage() {
		context.getScopeManager().dispatchMessage(new TriggerA());
	}

}
