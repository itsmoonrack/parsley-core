package org.spicefactory.parsley.core.scope.impl;

import org.spicefactory.parsley.core.bootstrap.BootstrapInfo;
import org.spicefactory.parsley.core.messaging.MessageReceiverCache;
import org.spicefactory.parsley.core.messaging.MessageReceiverRegistry;
import org.spicefactory.parsley.core.messaging.impl.DefaultMessageReceiverRegistry;
import org.spicefactory.parsley.core.scope.ScopeDefinition;
import org.spicefactory.parsley.core.scope.ScopeInfo;

/**
 * Default implementation of the ScopeInfo interface.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public class DefaultScopeInfo implements ScopeInfo {

	DefaultScopeInfo(ScopeDefinition definition, BootstrapInfo info) {
		this.name = definition.name();
		this.uuid = definition.uuid();
		this.inherited = definition.inherited();
		this.messageReceivers = new DefaultMessageReceiverRegistry();
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public MessageReceiverRegistry messageReceivers() {
		return messageReceivers;
	}

	@Override
	public MessageReceiverCache getMessageReceiverCache(Class<?> type) {
		return messageReceivers.getSelectionCache(type);
	}

	private final String name;
	private final String uuid;
	private final boolean inherited;
	private final DefaultMessageReceiverRegistry messageReceivers;

}
