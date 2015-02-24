package org.spicefactory.parsley.view;

import javax.inject.Inject;

import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.ProvisionException;

/**
 * Provides an API for injecting a managed object from the nearest Context in the view hierarchy into a view without reflecting on the view.
 * <p>
 * This API can be used in Java components (Swing) as an alternative to the <code>FastInject</code> FXML annotation for FXML components.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public class FastInject {

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	@Inject
	static Injector injector;

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	/**
	 * Returns the appropriate instance for the given injection type; equivalent to {@code getProvider(type).get()}.
	 * <p>
	 * When feasible, avoid using this method, in favor of having Guice inject your dependencies ahead of time.
	 * @throws ConfigurationException if this injector cannot find or create the provider.
	 * @throws ProvisionException if there was a runtime failure while providing an instance.
	 */
	public static <T> T getInstance(Class<? extends T> type) {
		return injector.getInstance(type);
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////
}
