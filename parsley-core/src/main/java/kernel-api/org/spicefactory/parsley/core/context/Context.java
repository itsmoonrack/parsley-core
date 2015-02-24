package org.spicefactory.parsley.core.context;

import javax.annotation.Nullable;

import org.spicefactory.lib.event.IEventDispatcher;
import org.spicefactory.parsley.core.scope.ScopeManager;
import org.spicefactory.parsley.core.view.ViewManager;

/**
 * Dispatched when configuration for this Context has been fully processed.
 * <p>
 * This includes loading configuration files, reflecting on classes, processing annotations or validating the configuration. After this property
 * is set the configuration of the Context is sealed and can no longer be modified.
 * <p>
 * Some ContextBuilder implementations execute synchronously. In this case this event will never fire. Thus before registering for this event you
 * should check the <code>isConfigured</code> method on the Context.
 * </p>
 */
// @Event(id = ContextEvent.CONFIGURED, type = ContextEvent.class)

/**
 * Dispatched when the Context was fully initialized.
 * <p>
 * At this point all configuration was processed and all non-lazy singleton objects in this Context have been instantiated and configured and
 * their asynchronous initializers (if any) have successfully completed.
 * <p>
 * Some ContextBuilder implementations execute synchronously. In this case this event will never fire. Thus before registering for this event you
 * should check the <code>isInitialized</code> method on the Context.
 * </p>
 */
// @Event(id = ContextEvent.INITIALIZED, type = ContextEvent.class)

/**
 * Dispatched when Context initialization failed.
 * <p>
 * This may happen due to errors in processing the configuration or because some asynchronous initializer on a non-lazy singleton failed. All
 * objects that have already been created at this point (partly or fully) will get their PreDestroy methods invoked.
 * <p>
 * After the <code>INITIALIZED</code> event of this Context has fired and the <code>initialized</code> property was set to true, this event can
 * never fire. In particular it does not fire if retrieving a lazy initializing object fails after Context initialization.
 * </p>
 */
// @Event(id = ContextEvent.ERROR, type = ContextEvent.class)

/**
 * Dispatched when the Context was destroyed.
 * <p>
 * At this point all methods marked with @Destroy on objects managed by this context have been invoked and any child Context instances were
 * destroyed, too.
 */
// Event(id = ContextEvent.DESTROYED, type = ContextEvent.class)

/**
 * The main interface of the IoC Container providing access to all configured objects.
 * <p>
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public interface Context extends IEventDispatcher<ContextListener> {

	/**
	 * Injects dependencies into the fields and methods of {@code instance}. Ignores the presence or absence of an injectable constructor.
	 * <p>
	 * Whenever Context creates an instance, it performs this injection automatically (after first performing constructor injection), so if
	 * you're able to let Context create all your objects for you, you'll never need to use this method.
	 * @param instance to inject members on
	 */
	void injectMembers(Object instance);

	/**
	 * Returns the appropriate instance for the given injection type; When feasible, avoid using this method, in favor of having Context inject
	 * your dependencies ahead of time.
	 */
	<T> T getInstance(Class<T> type);

	/**
	 * Gets parents Context. Null if top-level parent.
	 */
	@Nullable
	Context[] getParents();

	/**
	 * Gets scope manager that handles all scopes that this Context belongs to.
	 * <p>
	 * This includes scopes inherited from parent Contexts as well as scopes created for this Context.
	 */
	ScopeManager getScopeManager();

	/**
	 * Gets view manager used to dynamically wire view instances to this Context.
	 */
	ViewManager getViewManager();

	/**
	 * Indicates whether configuration for this Context has been fully processed.
	 * <p>
	 * This includes loading configuration files, reflecting on classes, processing annotations or validating the configuration. After this
	 * property is set the configuration of the Context is sealed an can no longer be modified. If you try to access the content of this Context
	 * with methods like <code>getObject</code> before this property has been set to true, an Error will be thrown. In case this property is set
	 * to false after Context creation you can listen to the <code>ContextEvent.CONFIGURED</code> event.
	 */
	boolean isConfigured();

	/**
	 * Indicates whether this Context has been fully initialized.
	 * <p>
	 * This includes processing the configuration (thus <code>configured</code> is always true if this property is true) and instantiating all
	 * objects that were configured as non-lazy singleton (the default). This includes processing all objects which were configured to be
	 * asynchronously initializing, waiting until all these objects have been fully initialized. In case this property is set to false after
	 * Context creation you can listen to the <code>ContextEvent.INITIALIZED</code> event.
	 * <p>
	 * The objects contained in this Context can already be accessed before this property is set to true (as long as the <code>configured</code>
	 * property is set to true). This is necessary since objects that are instantiated and configured during the initialization process of the
	 * Context may need access to their dependencies. But you should be aware that you might mix up the initialization order so you should use
	 * the Context with caution before this property is set to true.
	 * </p>
	 */
	boolean isInitialized();

	/**
	 * Indicates whether this Context has been destroyed.
	 * <p>
	 * When this property is set to true, the Context can no longer be used and most of its methods will throw an Error.
	 */
	boolean isDestroyed();

	/**
	 * Destroys this Context. This includes processing all life-cycle listeners for all objects that this Context has instantiated and calling
	 * their methods marked with <code>@Destroy</code>. The Context may no longer be used after calling this method.
	 */
	void destroy();

}
