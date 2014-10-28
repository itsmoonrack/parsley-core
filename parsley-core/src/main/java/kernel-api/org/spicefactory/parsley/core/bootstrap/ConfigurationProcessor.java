package org.spicefactory.parsley.core.bootstrap;

import org.spicefactory.parsley.core.context.Context;

/**
 * Responsible for processing configuration and adding object definitions to a context.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface ConfigurationProcessor {

	/**
	 * Processes all configuration artifacts.
	 * @param context the context to process
	 */
	void processConfiguration(Context context);

}
