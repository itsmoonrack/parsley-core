package org.spicefactory.parsley.guice.processor;

import org.spicefactory.parsley.core.bootstrap.ConfigurationProcessor;
import org.spicefactory.parsley.core.context.Context;

import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.Stage;

public class GuiceConfigurationProcessor implements ConfigurationProcessor {

	private final Stage stage;
	private final Module[] modules;

	public GuiceConfigurationProcessor(Module... modules) {
		this(Stage.DEVELOPMENT, modules);
	}

	public GuiceConfigurationProcessor(Stage stage, Module... modules) {
		this.stage = stage;
		this.modules = modules;
	}

	@Override
	public void processConfiguration(Context context) {
		injector = Guice.createInjector(stage, modules);
	}
}
