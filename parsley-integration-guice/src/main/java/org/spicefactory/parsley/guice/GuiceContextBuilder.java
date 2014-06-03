package org.spicefactory.parsley.guice;

import java.awt.Container;

import org.spicefactory.parsley.context.ContextBuilder;
import org.spicefactory.parsley.core.context.Context;

import com.google.inject.Module;
import com.google.inject.Stage;

/**
 * Static entry point methods for building a Context from Guice configuration classes.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public class GuiceContextBuilder {

	/**
	 * Builds a Context from the specified Guice configuration class.
	 * <p>
	 * @param viewRoot the initial view root for dynamically wiring view objects
	 * @param modules the class that contains the Guice configuration
	 * @return a new Context instance
	 */
	public static Context build(Container viewRoot, Stage stage, Module... modules) {
		return ContextBuilder //
				.newSetup() //
				.viewRoot(viewRoot) //
				.newBuilder() //
				.config(GuiceConfig.forModules(stage, modules)) //
				.build();
	}

}
