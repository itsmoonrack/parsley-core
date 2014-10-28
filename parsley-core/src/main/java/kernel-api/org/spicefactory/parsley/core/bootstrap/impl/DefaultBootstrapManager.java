package org.spicefactory.parsley.core.bootstrap.impl;

import org.spicefactory.parsley.core.bootstrap.BootstrapConfig;
import org.spicefactory.parsley.core.bootstrap.BootstrapManager;
import org.spicefactory.parsley.core.bootstrap.BootstrapProcessor;

public class DefaultBootstrapManager implements BootstrapManager {

	private final DefaultBootstrapConfig config = null;

	@Override
	public BootstrapConfig config() {
		return config;
	}

	@Override
	public BootstrapProcessor createProcessor() {
		return config.createProcessor();
	}

}
