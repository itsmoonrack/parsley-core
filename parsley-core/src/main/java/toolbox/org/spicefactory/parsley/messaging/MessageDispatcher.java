package org.spicefactory.parsley.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spicefactory.parsley.core.messaging.Selector;
import org.spicefactory.parsley.core.scope.Scope;
import org.spicefactory.parsley.core.scope.ScopeManager;

/**
 * Represents a reference to a message dispatcher function. To be used in FXML and XML configuration.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
class MessageDispatcher implements Dispatcher {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final ScopeManager scopeManager;
	private final String scope;
	private final Object owner;

	private volatile boolean enabled = true;

	/**
	 * Creates a new instance.
	 * @param scopeManager the scope manager the message should be dispatched through
	 * @param scope the scope the message should be dispatched to
	 * @param owner the owner of this dispatcher
	 */
	MessageDispatcher(ScopeManager scopeManager, String scope, Object owner) {
		this.scopeManager = scopeManager;
		this.scope = scope;
		this.owner = owner;
	}

	@Override
	public void dispatchMessage(Object message) {
		dispatchMessage(message, Selector.NONE);
	}

	@Override
	public void dispatchMessage(Object message, int selector) {
		if (!enabled) {
			logger.warn("Attempt to use message dispatcher for {} after it has been disabled.", owner);
			return;
		}
		if (scope == Scope.GLOBAL_) {
			scopeManager.dispatchMessage(message, selector);
		} else {
			scopeManager.getScope(scope).dispatchMessage(message, selector);
		}
	}

	/**
	 * Disables this dispatcher so that calls to dispatchMessage get ignored.
	 */
	public void disable() {
		enabled = false;
	}

}
