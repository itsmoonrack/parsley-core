package org.spicefactory.parsley.core.scope.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.spicefactory.parsley.core.bootstrap.BootstrapInfo;
import org.spicefactory.parsley.core.command.ObservableCommand;
import org.spicefactory.parsley.core.messaging.Message;
import org.spicefactory.parsley.core.messaging.MessageReceiverCache;
import org.spicefactory.parsley.core.messaging.MessageReceiverKind;
import org.spicefactory.parsley.core.messaging.MessageRouter;
import org.spicefactory.parsley.core.messaging.impl.DefaultMessage;
import org.spicefactory.parsley.core.messaging.receiver.MessageReceiver;
import org.spicefactory.parsley.core.scope.Scope;
import org.spicefactory.parsley.core.scope.ScopeDefinition;
import org.spicefactory.parsley.core.scope.ScopeInfo;
import org.spicefactory.parsley.core.scope.ScopeInfoRegistry;
import org.spicefactory.parsley.core.scope.ScopeManager;

/**
 * Default implementation of the ScopeManager interface.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
@Singleton
public class DefaultScopeManager implements ScopeManager {

	@Inject
	private Logger logger;

	private final Map<String, Scope> scopes;
	private final MessageRouter messageRouter;
	private final ScopeInfoRegistry scopeInfoRegistry;

	@Inject
	DefaultScopeManager(ScopeInfoRegistry scopeInfoRegistry, MessageRouter messageRouter) {
		this.scopes = new HashMap<String, Scope>();
		this.messageRouter = messageRouter;
		this.scopeInfoRegistry = scopeInfoRegistry;

		initScopes(null);
	}

	/**
	 * Creates all scopes managed by this instance.
	 * <p>
	 * The given BootstrapInfo instance provides information about all scopes inherited by parent Contexts and all new scope definitions added
	 * for this Context. The default implementation manages all these scopes and throws an error in case of finding more than one scope with the
	 * same name.
	 * </p>
	 * <p>
	 * This method can be overridden by custom ScopeManagers to allow fine-grained control which scopes should be inherited and/or created.
	 * </p>
	 * @param info a BootstrapInfo instance providing information about all scopes inherited by parent Contexts and all new scope definitions
	 *            added for this Context
	 */
	protected void initScopes(/* TODO: not used. */BootstrapInfo info) {
		for (ScopeInfo scopeInfo : scopeInfoRegistry.getParentScopes()) {
			addScope(scopeInfo, info);
		}
		for (ScopeDefinition scopeDef : scopeInfoRegistry.getNewScopes()) {
			DefaultScopeInfo newScope = new DefaultScopeInfo(scopeDef, info);
			addScope(newScope, info);
		}
	}

	/**
	 * Adds the specified scope to the active scopes of this manager.
	 * @param scopeInfo the scope to add to this manager
	 * @param info the environment of the Context building process
	 */
	protected void addScope(ScopeInfo scopeInfo, /* TODO: not used. */BootstrapInfo info) {
		if (scopes.containsKey(scopeInfo.name())) {
			throw new IllegalStateException("Duplicate scope with name: " + scopeInfo.name());
		}
		scopeInfoRegistry.addActiveScope(scopeInfo);
		Scope scope = new DefaultScope(scopeInfo, messageRouter);
		scopes.put(scope.name(), scope);
	}

	@Override
	public boolean hasScope(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Scope getScope(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Scope> getAllScopes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dispatchMessage(Object instance, String selector) {
		final Class<?> type = instance.getClass();
		final List<ScopeInfo> scopes = scopeInfoRegistry.getActiveScopes();
		final List<MessageReceiverCache> caches = new ArrayList<MessageReceiverCache>(scopes.size());

		for (ScopeInfo scope : scopes) {
			caches.add(scope.getMessageReceiverCache(type));
		}
		final MessageReceiverCache cache = new MergedMessageReceiverCache(caches);

		if (selector == null) {
			selector = cache.getSelectorValue(instance);
		}
		final Message message = new DefaultMessage(instance, type, selector);

		if (cache.getReceivers(MessageReceiverKind.TARGET, selector).size() == 0) {
			logger.warn("Discarding message '{}': no matching receiver in any scope.", type);
			return;
		}

		messageRouter.dispatchMessage(message, cache);
	}

	@Override
	public void observeCommand(ObservableCommand command) {
		// TODO Auto-generated method stub

	}

	private class MergedMessageReceiverCache implements MessageReceiverCache {

		private final List<MessageReceiverCache> caches;

		public MergedMessageReceiverCache(List<MessageReceiverCache> caches) {
			this.caches = caches;
		}

		@Override
		public Set<MessageReceiver> getReceivers(MessageReceiverKind kind, String selector) {
			Set<MessageReceiver> receivers = new HashSet<MessageReceiver>();
			for (MessageReceiverCache cache : caches) {
				receivers.addAll(cache.getReceivers(kind, selector));
			}
			return receivers;
		}

		@Override
		public String getSelectorValue(Object message) {
			// TODO Auto-generated method stub
			return null;
		}

	}

}
