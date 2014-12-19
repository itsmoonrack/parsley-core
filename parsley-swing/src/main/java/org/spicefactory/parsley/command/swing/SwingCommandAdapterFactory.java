package org.spicefactory.parsley.command.swing;

import java.lang.reflect.Method;

import org.spicefactory.lib.command.adapter.CommandAdapter;
import org.spicefactory.lib.command.adapter.CommandAdapterFactory;
import org.spicefactory.parsley.command.annotation.Async;

/**
 * A CommandAdapterFactory implementation that creates adapters from commands that adhere to the conventions of Spicelib's "Swing Commands".
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public class SwingCommandAdapterFactory implements CommandAdapterFactory {

	@Override
	public CommandAdapter createAdapter(Object instance) {
		Method execute = null;
		Method cancel = null;
		Method result = null;
		Method error = null;
		boolean async = instance.getClass().isAnnotationPresent(Async.class);

		for (Method m : instance.getClass().getMethods()) {
			if ("execute".equals(m.getName())) {
				execute = m;
			}
			if ("cancel".equals(m.getName()) && m.getParameterTypes().length == 0) {
				cancel = m;
			}
			if ("result".equals(m.getName()) && m.getParameterTypes().length == 1) {
				result = m;
			}
			if ("error".equals(m.getName()) && m.getParameterTypes().length == 1) {
				error = m;
			}
		}

		return new SwingCommandAdapter(instance, execute, cancel, result, error, async);
	}
}
