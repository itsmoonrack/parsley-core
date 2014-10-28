package org.spicefactory.parsley.core.bootstrap;

/**
 * The BootstrapManager is the entry point for building a new Context. It exposes the configuration instance that can be used to specify
 * settings, configuration processors and services and a method to create a processor that is responsible for applying the configuration and
 * initializing a new Context.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface BootstrapManager {

	/**
	 * The configuration to be used when building a new Context.
	 * <p>
	 * Can be used to specify settings, configuration processors and services and a method to create a processor that is responsible for applying
	 * the configuration and initializing a new Context.
	 */
	BootstrapConfig config();

	/**
	 * Creates a new processor responsible for applying the configuration and initializing a new Context.
	 * @return a ney processor responsible for applying the configuration and initializing a new Context
	 */
	BootstrapProcessor createProcessor();
}
