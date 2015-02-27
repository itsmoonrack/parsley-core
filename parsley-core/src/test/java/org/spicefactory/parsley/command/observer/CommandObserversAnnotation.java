package org.spicefactory.parsley.command.observer;

import javax.inject.Singleton;

import org.spicefactory.parsley.command.trigger.Trigger;
import org.spicefactory.parsley.command.trigger.TriggerA;
import org.spicefactory.parsley.command.trigger.TriggerB;
import org.spicefactory.parsley.comobserver.annotation.CommandComplete;
import org.spicefactory.parsley.comobserver.annotation.CommandException;
import org.spicefactory.parsley.comobserver.annotation.CommandResult;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
@Singleton
public class CommandObserversAnnotation extends CommandObservers {

	@Override
	@CommandComplete
	public void completeA(TriggerA trigger) {
		super.completeA(trigger);
	}

	@Override
	@CommandComplete
	public void completeB(TriggerB trigger) {
		super.completeB(trigger);
	}

	@Override
	@CommandComplete
	public void complete(Trigger trigger) {
		super.complete(trigger);
	}

	@Override
	@CommandResult(immediate = true)
	public void resultA(Object result, TriggerA trigger) {
		super.resultA(result, trigger);
	}

	@Override
	@CommandResult(immediate = true)
	public void resultB(Object result, TriggerB trigger) {
		super.resultB(result, trigger);
	}

	@Override
	@CommandResult(immediate = true)
	public void result(Object result, Trigger trigger) {
		super.result(result, trigger);
	}

	@Override
	@CommandException
	public void exception(Object result, Trigger trigger) {
		super.exception(result, trigger);
	}

}
