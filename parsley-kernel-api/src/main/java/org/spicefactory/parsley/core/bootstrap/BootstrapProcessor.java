package org.spicefactory.parsley.core.bootstrap;

import org.spicefactory.parsley.core.context.Context;

/**
 * Responsible for processing the configuration and initializing a Context.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public interface BootstrapProcessor {

	/**
	 * Adds a configuration processor.
	 * @param processor the processor to add
	 */
	void addProcessor(ConfigurationProcessor processor);

	/**
	 * Finally processes the configuration, applying all processors that were added and initializing the Context.
	 * <p>
	 * This process may be asynchronous. Listeners for the completion or error events can be registered with the returned Context.
	 * @return the Context, possibly not yet fully initialized
	 */
	Context process();

}
