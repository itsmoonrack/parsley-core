package org.spicefactory.parsley.core.scope.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spicefactory.parsley.core.messaging.Message;
import org.spicefactory.parsley.core.messaging.MessageReceiverCache;
import org.spicefactory.parsley.core.messaging.MessageReceiverKind;
import org.spicefactory.parsley.core.messaging.MessageRouter;
import org.spicefactory.parsley.core.messaging.impl.DefaultMessage;
import org.spicefactory.parsley.core.scope.Scope;
import org.spicefactory.parsley.core.scope.ScopeInfo;

/**
 * Default implementation of the Scope interface.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public class DefaultScope implements Scope {

	DefaultScope(ScopeInfo info, MessageRouter router) {
		this.info = info;
		this.messageRouter = router;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String uuid() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean inherited() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void dispatchMessage(Object instance, Object selector) {
		final Class<?> type = instance.getClass();
		final MessageReceiverCache cache = info.getMessageReceiverCache(type);

		if (selector == null) {
			selector = cache.getSelectorValue(instance);
		}
		final Message message = new DefaultMessage(instance, type, selector);

		if (cache.getReceivers(MessageReceiverKind.TARGET, message.selector()).size() == 0) {
			logger.warn("Discarding message '{}' for scope '{}': no matching receiver.", instance, this);
			return;
		}

		messageRouter.dispatchMessage(message, cache);
	}

	private final Logger logger = LoggerFactory.getLogger(DefaultScope.class);

	private final ScopeInfo info;
	private final MessageRouter messageRouter;
}
