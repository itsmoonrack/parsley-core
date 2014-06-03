package org.spicefactory.parsley.guice;

import org.spicefactory.parsley.core.bootstrap.ConfigurationProcessor;
import org.spicefactory.parsley.guice.processor.GuiceConfigurationProcessor;

import com.google.inject.Module;
import com.google.inject.Stage;

/**
 * Static entry point methods for creating Guice configuration processors to be added to a ContextBuilder.
 * <p>
 * Example:
 * </p>
 *
 * <pre>
 * <code>ContextBuilder
 *     .newInstance()
 *     .config(GuiceConfig.forClass(MyConfig.class))
 *     .build();</code>
 * </pre>
 */
public final class GuiceConfig {

	/**
	 * Creates a processor for the specified Module configuration class.
	 * @param configClasses the classes that contains the Module configuration
	 * @return a new configuration processor instance which can be added to a ContextBuilder
	 */
	public static ConfigurationProcessor forClass(Module... configClasses) {
		return new GuiceConfigurationProcessor(configClasses);
	}

	/**
	 * Creates a processor for the specified Module configuration class.
	 * @param configClasses the classes that contains the Module configuration
	 * @return a new configuration processor instance which can be added to a ContextBuilder
	 */
	public static ConfigurationProcessor forModules(Stage stage, Module... modules) {
		return new GuiceConfigurationProcessor(stage, modules);
	}
}
