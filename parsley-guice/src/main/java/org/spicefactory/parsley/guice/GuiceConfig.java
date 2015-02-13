package org.spicefactory.parsley.guice;

import java.util.Arrays;

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
 * 		.newBuilder()
 * 		.config(GuiceConfig.forModule(MyConfig.class)
 * 		.build();</code>
 * </pre>
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class GuiceConfig {

	/**
	 * Creates a processor for the specified Guice configuration module.
	 * @param module
	 */
	public static ConfigurationProcessor forModule(Module module) {
		return forModules(Stage.DEVELOPMENT, new Module[] {module});
	}

	/**
	 * Creates a processor for the specified Guice configuration module.
	 * @param modules
	 */
	public static ConfigurationProcessor forModules(Module... modules) {
		return forModules(Stage.DEVELOPMENT, modules);
	}

	/**
	 * Creates a processor for the specified Guice configuration module.
	 * @param module
	 */
	public static ConfigurationProcessor forModule(Stage stage, Module module) {
		return forModules(stage, new Module[] {module});
	}

	/**
	 * Creates a processor for the specified Guice configuration module.
	 * @param modules
	 */
	public static ConfigurationProcessor forModules(Stage stage, Module... modules) {
		return new GuiceConfigurationProcessor(stage, Arrays.asList(modules));
	}
}
