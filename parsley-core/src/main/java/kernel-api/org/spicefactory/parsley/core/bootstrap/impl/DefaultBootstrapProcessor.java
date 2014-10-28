package org.spicefactory.parsley.core.bootstrap.impl;

import java.util.ArrayList;
import java.util.List;

import org.spicefactory.parsley.core.bootstrap.BootstrapInfo;
import org.spicefactory.parsley.core.bootstrap.BootstrapProcessor;
import org.spicefactory.parsley.core.bootstrap.ConfigurationProcessor;
import org.spicefactory.parsley.core.context.Context;

public class DefaultBootstrapProcessor implements BootstrapProcessor {

	private ConfigurationProcessor currentProcessor;

	private final BootstrapInfo info;
	private final List<ConfigurationProcessor> processors;

	public DefaultBootstrapProcessor() {
		processors = new ArrayList<ConfigurationProcessor>();
		info = null;
	}

	@Override
	public void addProcessor(ConfigurationProcessor processor) {
		processors.add(processor);
	}

	@Override
	public Context process() {
		// TODO Auto-generated method stub
		return null;
	}

}
