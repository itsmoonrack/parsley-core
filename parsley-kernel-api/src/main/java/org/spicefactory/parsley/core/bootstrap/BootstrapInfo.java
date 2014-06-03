package org.spicefactory.parsley.core.bootstrap;

/**
 * Represents the environment for a single Context building process in a read-only way.
 * <p>
 * Implementations of this interface get passed to kernel services that implement the <code>InitializingService</code> interface. They can be
 * used to get access to collaborating services or other environment settings.
 * </p>
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface BootstrapInfo {

}
