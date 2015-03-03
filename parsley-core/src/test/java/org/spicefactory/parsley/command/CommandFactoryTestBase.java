package org.spicefactory.parsley.command;

import java.util.ArrayList;
import java.util.List;

import org.spicefactory.lib.command.proxy.CommandProxy;
import org.spicefactory.parsley.command.target.CommandBase;
import org.spicefactory.parsley.command.trigger.TriggerB;
import org.spicefactory.parsley.core.command.ObservableCommand;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public abstract class CommandFactoryTestBase extends CommandTestBase {

	private CommandProxy proxy;

	protected void setProxy(CommandProxy proxy) {
		this.proxy = proxy;
	}

	@Override
	protected List<ObservableCommand> getActiveCommands(Class<?> type) {
		if (type == TriggerB.class)
			return new ArrayList<ObservableCommand>(); // We do not test this scenario with factories.
		return manager.getActiveCommandsByType(CommandBase.class);
	}

	@Override
	protected void execute() {
		proxy.execute();
	}

}
