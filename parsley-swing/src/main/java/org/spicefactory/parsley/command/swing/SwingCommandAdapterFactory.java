package org.spicefactory.parsley.command.swing;

import java.lang.reflect.Method;

import org.spicefactory.parsley.core.command.adapter.CommandAdapter;
import org.spicefactory.parsley.core.command.adapter.CommandAdapterFactory;

public class SwingCommandAdapterFactory implements CommandAdapterFactory {

	@Override
	public CommandAdapter createAdapter(Object instance) {
		Method execute = null;

		for (Method m : instance.getClass().getMethods()) {
			if ("execute".equals(m.getName())) {
				execute = m;
			}
		}

		return new SwingCommandAdapter(instance, execute);
	}
}
