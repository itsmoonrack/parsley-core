package org.spicefactory.parsley.command.observer;

import java.util.ArrayList;
import java.util.List;

import org.spicefactory.parsley.command.trigger.Trigger;
import org.spicefactory.parsley.command.trigger.TriggerA;
import org.spicefactory.parsley.command.trigger.TriggerB;

/**
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class CommandObservers {

	public int completeInvoked, completeAInvoked, completeBInvoked;

	public List<Object> resultsA = new ArrayList<Object>();
	public List<Object> resultsB = new ArrayList<Object>();
	public List<Object> results = new ArrayList<Object>();

	public List<Object> exceptions = new ArrayList<Object>();

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	public void completeA(TriggerA trigger) {
		completeAInvoked++;
	}

	public void completeB(TriggerB trigger) {
		completeBInvoked++;
	}

	public void complete(Trigger trigger) {
		completeInvoked++;
	}

	public void resultA(Object result, TriggerA trigger) {
		resultsA.add(result);
	}

	public void resultB(Object result, TriggerB trigger) {
		resultsB.add(result);
	}

	public void result(Object result, Trigger trigger) {
		results.add(result);
	}

	public void exception(Object result, Trigger trigger) {
		exceptions.add(result);
	}

	public void reset() {
		completeInvoked = 0;
		completeAInvoked = 0;
		completeBInvoked = 0;

		results = new ArrayList<Object>();
		resultsA = new ArrayList<Object>();
		resultsB = new ArrayList<Object>();
		exceptions = new ArrayList<Object>();
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////
}
