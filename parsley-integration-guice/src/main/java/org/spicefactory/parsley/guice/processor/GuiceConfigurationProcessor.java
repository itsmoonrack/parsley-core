package org.spicefactory.parsley.guice.processor;

import org.spicefactory.parsley.core.bootstrap.ConfigurationProcessor;
import org.spicefactory.parsley.core.context.Context;
import org.spicefactory.parsley.guice.GuiceParsleyTypeListener;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;

public class GuiceConfigurationProcessor implements ConfigurationProcessor {

	private Injector injector;

	private final Stage stage;
	private final Iterable<? extends Module> modules;

	public GuiceConfigurationProcessor(Stage stage, Iterable<? extends Module> modules) {
		this.stage = stage;
		this.modules = modules;
	}

	@Override
	public void processConfiguration(Context context) {
		injector = Guice.createInjector(stage, modules);
		GuiceParsleyTypeListener test = injector.get
	}

	public Injector getInjector() {
		return injector;
	}
}
