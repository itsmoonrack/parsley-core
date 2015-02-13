package org.spicefactory.parsley.command.observer;

import java.util.Date;

import org.spicefactory.lib.command.data.CommandData;
import org.spicefactory.parsley.command.trigger.Trigger;
import org.spicefactory.parsley.comobserver.annotation.CommandResult;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class CommandObserversNonImmediate {

	public boolean firstResult;
	public boolean secondResult;
	public boolean allResults;

	@CommandResult
	public void setFirstResult(String result, Trigger trigger) {
		firstResult = true;
	}

	@CommandResult
	public void setSecondResult(Date result, Trigger trigger) {
		secondResult = true;
	}

	@CommandResult
	public void setFirstResult(CommandData result, Trigger trigger) {
		allResults = true;
	}

}
