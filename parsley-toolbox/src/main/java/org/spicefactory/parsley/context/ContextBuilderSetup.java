package org.spicefactory.parsley.context;

import java.awt.Container;

/**
 * Allows to specify the options for a ContextBuilder before creating it.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public final class ContextBuilderSetup {

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

	/**
	 * Creates a new ContextBuilder based on the settings of this setup instance.
	 * @return a new ContextBuilder based on the settings of this setup instance
	 */
	public ContextBuilder newBuilder() {
		return null;
	}

	private Container viewRoot;

}
