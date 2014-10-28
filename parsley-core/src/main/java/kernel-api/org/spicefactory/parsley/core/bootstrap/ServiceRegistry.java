package org.spicefactory.parsley.core.bootstrap;

import org.spicefactory.parsley.core.bootstrap.impl.DefaultService;
import org.spicefactory.parsley.core.context.Context;

/**
 * The registry for the configurations of all seven IOC kernel services for one particular Context.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class ServiceRegistry {

	/**
	 * The configuration for the service that handles the initialization of a Context.
	 */
	public final Service<BootstrapManager> bootstrapManager = new DefaultService<BootstrapManager>();

	/**
	 * The configuration for the Context implementation.
	 */
	public final Service<Context> context = new DefaultService<Context>();

}
