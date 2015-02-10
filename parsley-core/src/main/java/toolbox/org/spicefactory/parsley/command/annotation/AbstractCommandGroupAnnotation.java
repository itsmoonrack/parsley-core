package org.spicefactory.parsley.command.annotation;

import java.util.LinkedList;
import java.util.List;

import org.spicefactory.lib.command.group.CommandGroup;
import org.spicefactory.parsley.command.DefaultManagedCommandProxy;
import org.spicefactory.parsley.core.command.ManagedCommandFactory;
import org.spicefactory.parsley.core.command.ManagedCommandProxy;
import org.spicefactory.parsley.core.context.Context;

/**
 * Base annotation for command groups declared in FXML or XML configuration.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public abstract class AbstractCommandGroupAnnotation extends AbstractCommandParentAnnotation implements NestedCommandAnnotation {

	private final Class<?> type;

	protected AbstractCommandGroupAnnotation(Class<?> type) {
		this.type = type;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	@Override
	public ManagedCommandFactory resolve(Context context) {
		List<ManagedCommandFactory> factories = new LinkedList<ManagedCommandFactory>();
		for (NestedCommandAnnotation annotation : commands) {
			factories.add(annotation.resolve(context));
		}
		return new Factory(type, id, factories, context);
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	private class Factory implements ManagedCommandFactory {

		private final int id;
		private final Class<?> type;
		private final Context context;
		private final List<ManagedCommandFactory> factories;

		public Factory(Class<?> type, int id, List<ManagedCommandFactory> factories, Context context) {
			this.id = id;
			this.type = type;
			this.context = context;
			this.factories = factories;
		}

		@Override
		public Class<?> type() {
			return type;
		}

		@Override
		public ManagedCommandProxy newInstance() {
			try {
				CommandGroup group = (CommandGroup) type.newInstance();
				for (ManagedCommandFactory factory : factories) {
					group.addCommand(factory.newInstance());
				}
				return new DefaultManagedCommandProxy(context, group, id);
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

	}
}
