package org.spicefactory.parsley.core.processor;

import javax.inject.Provider;

import org.spicefactory.parsley.core.context.Context;

/**
 * Responsible for processing an object when its gets initialized or destroyed.
 * <p>
 * This is one of the major hooks that configuration annotations and other mechanisms may use to configure an object. The <code>init</code>
 * method usually sets properties, registers message receivers or performs similar configuration tasks for a target object.
 * <p>
 * If the container should manage a separate processor instance for each target instance the processor also has to implement the
 * <code>StatefulProcessor</code> interface.
 * </p>
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public interface ObjectProcessor {

	/**
	 * Invoked during initialization of the target instance.
	 * <p>
	 * Implementations will usually set properties, registers message receivers or performs similar configuration tasks for the target instance
	 * managed by this processor.
	 * @param target the target instance that is getting initialized
	 */
	void init(Provider<Object> provider, Provider<Context> context);

	/**
	 * Invoked when the target instance gets removed from the Context.
	 * <p>
	 * Implementations will usually unregister message receivers, un-watch bindings, remove listeners or perform similar disposal tasks.
	 * @param target the target instance that is getting removed from the Context
	 */
	void destroy(Object provider, Context context);

}
