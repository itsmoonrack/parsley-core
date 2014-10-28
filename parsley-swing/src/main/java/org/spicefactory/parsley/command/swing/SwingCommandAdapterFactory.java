package org.spicefactory.parsley.command.swing;

import java.lang.reflect.Method;

import org.spicefactory.lib.command.adapter.CommandAdapter;
import org.spicefactory.lib.command.adapter.CommandAdapterFactory;

/**
 * A CommandAdapterFactory implementation that creates adapters from commands that adhere to the conventions of Spicelib's "Swing Commands".
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
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
