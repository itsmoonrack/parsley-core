package org.spicefactory.parsley.command;

import java.lang.reflect.Method;

import javax.inject.Provider;

import org.spicefactory.lib.command.builder.CommandProxyBuilder;
import org.spicefactory.parsley.command.impl.CommandTriggerProvider;
import org.spicefactory.parsley.command.impl.DefaultManagedCommandProxy;
import org.spicefactory.parsley.command.impl.MappedCommandProxy;
import org.spicefactory.parsley.core.command.ManagedCommandFactory;
import org.spicefactory.parsley.core.command.ManagedCommandProxy;
import org.spicefactory.parsley.core.context.Context;
import org.spicefactory.parsley.core.context.ContextListener;
import org.spicefactory.parsley.core.events.ContextEvent;
import org.spicefactory.parsley.core.messaging.receiver.MessageTarget;
import org.spicefactory.parsley.core.scope.Scope;
import org.spicefactory.parsley.messaging.receiver.MessageReceiverInfo;

/**
 * A builder DSL for mapping commands to messages.
 * <p>
 * This API adds on top of the stand-alone Spicelib Commands builder APIs the ability to let commands get managed by a Parsley Context during
 * execution and to trigger execution based on a message.
 * </p>
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class MappedCommandBuilder implements ContextListener {

	private final ManagedCommandFactory factory;
	private final MessageReceiverInfo info = new MessageReceiverInfo();

	private String scope = Scope.GLOBAL;
	private Class<?> messageType = Object.class;
	private MessageTarget target;

	private MappedCommandBuilder(ManagedCommandFactory factory) {
		this.factory = factory;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	/** The default command trigger provider (Can be overridden by extension frameworks). */
	static CommandTriggerProvider commandTriggerProvider = new DefaultCommandTriggerProvider();

	static MappedCommandBuilder forFactory(ManagedCommandFactory factory) {
		return new MappedCommandBuilder(factory);
	}

	static <T> MappedCommandBuilder forType(Class<T> type) {
		return new MappedCommandBuilder(new Factory<T>(type));
	}

	static <T> MappedCommandBuilder forProvider(Provider<T> provider, Class<? extends T> type) {
		return new MappedCommandBuilder(new Factory<T>(type, provider));
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
			((Factory<?>) factory).init(context);

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

	private static class Factory<T> implements ManagedCommandFactory {

		private Context context;
		private Provider<T> provider;
		private final Class<? extends T> type;

		/////////////////////////////////////////////////////////////////////////////
		// Package-private.
		/////////////////////////////////////////////////////////////////////////////

		/////////////////////////////////////////////////////////////////////////////
		// Public API.
		/////////////////////////////////////////////////////////////////////////////

		public Factory(Class<T> type) {
			this.type = type;
		}

		public Factory(Class<? extends T> type, Provider<T> provider) {
			this.type = type;
			this.provider = provider;
		}

		public void init(Context context) {
			this.context = context;
		}

		@Override
		public Class<? extends T> type() {
			return type;
		}

		@Override
		public ManagedCommandProxy newInstance() {
			Object target = provider == null ? type : provider.get();
			DefaultManagedCommandProxy proxy = new DefaultManagedCommandProxy(context);
			CommandProxyBuilder builder = new CommandProxyBuilder(target, proxy);
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

}
