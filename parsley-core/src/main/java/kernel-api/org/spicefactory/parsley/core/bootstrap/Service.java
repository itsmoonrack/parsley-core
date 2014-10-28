package org.spicefactory.parsley.core.bootstrap;

/**
 * Represents the configuration of a single IOC kernel service.
 * <p>
 * It allows to either specify or replace the implementation of this service or add decorators.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 * @param <T>
 */
public interface Service<T> {

	/**
	 * Sets the implementation for this service, replacing the existing registration.
	 * @param type the implementation type of the service
	 * @param args the parameters type to pass to the constructor of the service
	 */
	void setImplementation(Class<? extends T> type, Class<?>... parameterTypes);

	/**
	 * Creates a new instance of this service, using the specified implementation and applying all decorators added to this service.
	 * @return
	 */
	T newInstance(Object... parameters);
}
