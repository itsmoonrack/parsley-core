package org.spicefactory.parsley.context;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

import org.spicefactory.parsley.core.bootstrap.BootstrapConfig;
import org.spicefactory.parsley.core.bootstrap.BootstrapDefaults;
import org.spicefactory.parsley.core.bootstrap.BootstrapManager;
import org.spicefactory.parsley.core.context.Context;

/**
 * Allows to specify the options for a ContextBuilder before creating it.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public final class ContextBuilderSetup {

	private Container viewRoot;

	/**
	 * Sets the view root the target Context should use to listen for bubbling events from components below that wish to get wired to the
	 * Context.
	 * @param viewRoot the view root for the target Context
	 * @return this builder instance for method chaining
	 */
	public ContextBuilderSetup viewRoot(Container viewRoot) {
		this.viewRoot = viewRoot;
		return this;
	}

	private final List<Context> parents = new ArrayList<Context>();

	/**
	 * Sets the parent to use for the target Context to look-up shared dependencies.
	 * <p>
	 * Will usually automatically detected in the view hierarchy and only has to be specified if this is not possible, for example when the
	 * parent Context is not associated with a view root Container.
	 * @param parent the parent to use for the target Context to look-up shared dependencies
	 * @return this builder instance for method chaining
	 */
	public ContextBuilderSetup parent(Context parent) {
		parents.add(parent);
		return this;
	}

	/**
	 * Creates a new ContextBuilder based on the settings of this setup instance.
	 * @return a new ContextBuilder based on the settings of this setup instance
	 */
	public ContextBuilder newBuilder() {
		final BootstrapManager manager = BootstrapDefaults.config().services.bootstrapManager.newInstance();
		final BootstrapConfig config = manager.config();
		config.viewRoot = viewRoot;
		for (Context parent : parents) {
			config.addParent(parent);
		}
		return new ContextBuilder(manager.createProcessor());
	}

}
