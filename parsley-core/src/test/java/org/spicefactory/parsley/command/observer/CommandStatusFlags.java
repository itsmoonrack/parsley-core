package org.spicefactory.parsley.command.observer;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class CommandStatusFlags {

	private boolean trigger;
	private boolean triggerA;
	private boolean triggerB;

	/////////////////////////////////////////////////////////////////////////////
	// Using getter/setter here so that we can override them in subclasses.
	/////////////////////////////////////////////////////////////////////////////

	public boolean getTrigger() {
		return trigger;
	}

	public void setTrigger(boolean trigger) {
		this.trigger = trigger;
	}

	public boolean getTriggerA() {
		return triggerA;
	}

	public void setTriggerA(boolean triggerA) {
		this.triggerA = triggerA;
	}

	public boolean getTriggerB() {
		return triggerB;
	}

	public void setTriggerB(boolean triggerB) {
		this.triggerB = triggerB;
	}

}
