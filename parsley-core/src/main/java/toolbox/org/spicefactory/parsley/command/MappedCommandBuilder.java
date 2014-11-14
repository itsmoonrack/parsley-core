package org.spicefactory.parsley.command;

import java.lang.reflect.Method;

import org.spicefactory.lib.command.Command;
import org.spicefactory.lib.command.builder.CommandBuilder;
import org.spicefactory.lib.command.builder.Commands;
import org.spicefactory.lib.command.proxy.CommandProxy;
import org.spicefactory.parsley.core.command.ManagedCommandFactory;
import org.spicefactory.parsley.core.command.ManagedCommandProxy;
import org.spicefactory.parsley.core.context.Context;
import org.spicefactory.parsley.core.context.ContextListener;
import org.spicefactory.parsley.core.events.ContextEvent;
import org.spicefactory.parsley.core.messaging.receiver.MessageTarget;
import org.spicefactory.parsley.messaging.receiver.MessageReceiverInfo;

/**
 * A builder DSL for mapping commands to messages.
 * <p>
 * This API adds on top of the standalone Spicelib Commands builder APIs the ability to let commands get managed by a Parsley Context during
 * execution and to trigger execution based on a message.
 * </p>
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public final class MappedCommandBuilder implements ContextListener {

	private final ManagedCommandFactory factory;
	private final MessageReceiverInfo info = new MessageReceiverInfo();

	private String scope;
	private Class<?> messageType;
	private MessageTarget target;

	private MappedCommandBuilder(ManagedCommandFactory factory) {
		this.factory = factory;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	/** The default command trigger provider (Can be overridden by extension frameworks). */
	static CommandTriggerProvider commandTriggerProvider = new DefaultCommandTriggerProvider();

	public static MappedCommandBuilder forFactory(ManagedCommandFactory factory) {
		return new MappedCommandBuilder(factory);
	}

	public static MappedCommandBuilder forType(Class<?> type) {
		return new MappedCommandBuilder(new Factory(type));
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	/**
	 * The type of message (including sub-types) that should trigger command execution.
	 * @param type the type of message that should trigger command execution.
	 * @return this builder instance for method chaining
	 */
	public MappedCommandBuilder messageType(Class<?> type) {
		messageType = type;
		return this;
	}

	/**
	 * The optional selector for mapping matching messages.
	 * @param selector the selector to use for matching messages.
	 * @return this builder instance for method chaining
	 */
	public MappedCommandBuilder selector(Object selector) {
		info.selector = selector;
		return this;
	}

	/**
	 * The execution order in relation to other message receivers.
	 * <p>
	 * This order attribute affects all types of message receivers, not only those that execute commands.
	 * </p>
	 * @param order the execution order in relation to other message receivers.
	 * @return this builder instance for method chaining
	 */
	public MappedCommandBuilder order(int order) {
		info.order = order;
		return this;
	}

	/**
	 * The name of the scope in which to listen for messages.
	 * @param scope the name of the scope in which to listen for messages.
	 * @return this builder instance for method chaining
	 */
	public MappedCommandBuilder scope(String scope) {
		this.scope = scope;
		return this;
	}

	/**
	 * Registers this mapping with the specified Context.
	 * @param context the Context this mapping is applied to.
	 */
	public void register(Context context) {
		if (factory instanceof Factory)
			((Factory) factory).init(context);

		info.type = (messageType == Object.class) ? deduceMessageType() : messageType;
		target = new MappedCommandProxy(factory, context, info);
		context.getScopeManager().getScope(scope).getMessageReceivers().addTarget(target);
		context.addEventListener(ContextEvent.DESTROYED, this);
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	private Class<?> deduceMessageType() {
		return commandTriggerProvider.getTriggerType(factory.type());
	}

	@Override
	public void process(ContextEvent event) {
		event.getContext().getScopeManager().getScope(scope).getMessageReceivers().removeTarget(target);
	}

	private static class Factory implements ManagedCommandFactory {

		private Context context;
		private final Class<?> type;

		/////////////////////////////////////////////////////////////////////////////
		// Package-private.
		/////////////////////////////////////////////////////////////////////////////

		/////////////////////////////////////////////////////////////////////////////
		// Public API.
		/////////////////////////////////////////////////////////////////////////////

		public Factory(Class<?> type) {
			this.type = type;
		}

		public void init(Context context) {
			this.context = context;
		}

		@Override
		public Class<?> type() {
			return type;
		}

		@Override
		public ManagedCommandProxy newInstance() {
			DefaultManagedCommandProxy proxy = new DefaultManagedCommandProxy(context);
			CommandProxyBuilder builder = new CommandProxyBuilder(type, proxy);
			builder.build();
			return proxy;
		}

		/////////////////////////////////////////////////////////////////////////////
		// Internal implementation.
		/////////////////////////////////////////////////////////////////////////////
	}

	private static class DefaultCommandTriggerProvider implements CommandTriggerProvider {

		/////////////////////////////////////////////////////////////////////////////
		// Package-private.
		/////////////////////////////////////////////////////////////////////////////

		/////////////////////////////////////////////////////////////////////////////
		// Public API.
		/////////////////////////////////////////////////////////////////////////////

		@Override
		public Class<?> getTriggerType(Class<?> commandType) {
			Method execute = null;

			for (Method m : commandType.getMethods()) {
				if ("execute".equals(m.getName())) {
					execute = m;
					break;
				}
			}

			if (execute == null)
				return null;

			return getFirstParameterType(execute);
		}

		/////////////////////////////////////////////////////////////////////////////
		// Internal implementation.
		/////////////////////////////////////////////////////////////////////////////

		private Class<?> getFirstParameterType(Method m) {
			return m.getParameterTypes().length > 0 ? m.getParameterTypes()[0] : null;
		}
	}

	private static class CommandProxyBuilder implements CommandBuilder {

		private final Object target;
		private final DefaultManagedCommandProxy proxy;

		public CommandProxyBuilder(Object target, DefaultManagedCommandProxy proxy) {
			this.proxy = proxy;
			this.target = target;
		}

		@Override
		public CommandProxy execute() {
			CommandProxy proxy = build();
			proxy.execute();
			return proxy;
		}

		@Override
		public CommandProxy build() {
			if (target instanceof Class<?>) {
				proxy.setType((Class<?>) target);
			} else {
				// TODO: This is not supported yet.
				proxy.setTarget(asCommand(target));
			}

			return proxy;
		}

		/**
		 * Turns the specified instance into a command that can be executed by the proxy created by this builder. Legal parameters are any
		 * instances that implement either <code>Command</code> or <code>CommandBuilder</code>, a <code>Class</code> reference that specifies the
		 * type of the target command to create, or any other type in case an adapter is registered that knows how to turn the type into a
		 * command.
		 * @param the instance to turn into a command
		 * @return the command created from the specified instance
		 */
		private Command asCommand(Object command) {
			if (command instanceof Command) {
				return (Command) command;
			} else if (command instanceof CommandBuilder) {
				return ((CommandBuilder) command).build();
			} else if (command instanceof Class<?>) {
				return Commands.create((Class<?>) command).build();
			}
			// TODO: else ....

			return null;
		}

	}

}
