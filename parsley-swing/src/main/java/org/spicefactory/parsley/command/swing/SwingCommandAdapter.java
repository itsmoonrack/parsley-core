package org.spicefactory.parsley.command.swing;

import java.lang.reflect.Method;

import javax.swing.SwingWorker;

import org.spicefactory.lib.command.adapter.CommandAdapter;

class SwingCommandAdapter extends SwingWorker<Object, Void> implements CommandAdapter {

	private final Object target;
	private final Method execute;

	public SwingCommandAdapter(Object target, Method execute) {
		this.target = target;
		this.execute = execute;
	}

	@Override
	protected Object doInBackground() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getTarget() {
		return target;
	}

}
