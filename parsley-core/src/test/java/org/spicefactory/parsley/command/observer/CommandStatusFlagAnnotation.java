package org.spicefactory.parsley.command.observer;

import javax.inject.Singleton;

import org.spicefactory.parsley.command.trigger.Trigger;
import org.spicefactory.parsley.command.trigger.TriggerA;
import org.spicefactory.parsley.command.trigger.TriggerB;
import org.spicefactory.parsley.comobserver.annotation.CommandStatus;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
@Singleton
public class CommandStatusFlagAnnotation extends CommandStatusFlags {


	@Override
	@CommandStatus(type = Trigger.class)
	public void setTrigger(boolean trigger) {
		super.setTrigger(trigger);
	}

	@Override
	@CommandStatus(type = TriggerA.class)
	public void setTriggerA(boolean triggerA) {
		super.setTriggerA(triggerA);
	}

	@Override
	@CommandStatus(type = TriggerB.class)
	public void setTriggerB(boolean triggerB) {
		super.setTriggerB(triggerB);
	}

}
