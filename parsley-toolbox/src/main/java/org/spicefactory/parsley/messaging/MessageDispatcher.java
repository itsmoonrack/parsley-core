package org.spicefactory.parsley.messaging;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spicefactory.parsley.core.scope.Scope;
import org.spicefactory.parsley.core.scope.ScopeManager;

public final class MessageDispatcher extends FunctionDispatcher {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final String scope;
	private final ScopeManager scopeManager;

	private volatile boolean enabled = true;

	public MessageDispatcher(ScopeManager scopeManager, @Nullable String scope) {
		this.scope = scope;
		this.scopeManager = scopeManager;
	}

	@Override
	public void dispatchMessage(Object message, @Nullable Object selector) {
		if (!enabled) {
			logger.warn("Attempt to use message dispatched after it has been disabled.");
		}
		if (scope == Scope.GLOBAL) {
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
