package org.spicefactory.parsley.guice.processor;

import org.spicefactory.parsley.core.bootstrap.ConfigurationProcessor;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;

public class GuiceConfigurationProcessor implements ConfigurationProcessor {

	private final Injector injector;

	public GuiceConfigurationProcessor(Module... modules) {
		this(Stage.PRODUCTION, modules);
	}

	public GuiceConfigurationProcessor(Stage stage, Module... modules) {
		injector = Guice.createInjector(stage, modules);
	}
}
