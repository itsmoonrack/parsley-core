package org.spicefactory.parsley.core.messaging.impl;

import java.text.MessageFormat;
import java.util.List;

import org.spicefactory.parsley.core.command.CommandObserverProcessor;
import org.spicefactory.parsley.core.command.CommandStatus;
import org.spicefactory.parsley.core.command.ObservableCommand;
import org.spicefactory.parsley.core.messaging.MessageReceiverCache;
import org.spicefactory.parsley.core.messaging.MessageReceiverKind;
import org.spicefactory.parsley.core.messaging.MessageSettings;
import org.spicefactory.parsley.core.messaging.receiver.CommandObserver;
import org.spicefactory.parsley.core.messaging.receiver.MessageReceiver;

class DefaultCommandObserverProcessor extends DefaultMessageProcessor implements CommandObserverProcessor {

	private Object result;
	private CommandStatus status;

	private final ObservableCommand observable;
	private final MessageReceiverCache typeCache;

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	DefaultCommandObserverProcessor(ObservableCommand observable, MessageReceiverCache typeCache, MessageReceiverCache triggerCache,
			MessageSettings settings) {
		super(observable.trigger(), triggerCache, settings);

		this.typeCache = typeCache;
		this.observable = observable;
		this.status = observable.status();
		this.result = observable.result();
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	@Override
	public Object command() {
		return observable.command();
	}

	@Override
	public Object result() {
		return result;
	}

	@Override
	public CommandStatus commandStatus() {
		return status;
	}

	@Override
	public boolean root() {
		return observable.root();
	}

	@Override
	public void changeResult(Object result, boolean error) {
		if (commandStatus() == CommandStatus.EXECUTE) {
			throw new IllegalStateException("Cannot set the result while command is still executing.");
		}
		this.result = result;
		this.status = (error) ? CommandStatus.ERROR : CommandStatus.COMPLETE;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	@Override
	protected List<MessageReceiver> fetchReceivers() {
		List<MessageReceiver> receivers = typeCache.getReceivers(MessageReceiverKind.forCommandStatus(commandStatus(), false), observable.id());

		if (observable.trigger() != null) {
			receivers.addAll(cache.getReceivers(MessageReceiverKind.forCommandStatus(commandStatus(), true), observable.trigger().selector()));
		}

		return receivers;
	}

	@Override
	protected void invokeReceiver(MessageReceiver observer) {
		CommandStatus oldStatus = commandStatus();
		((CommandObserver) observer).observeCommand(this);
		if (oldStatus != commandStatus() && oldStatus != CommandStatus.EXECUTE) {
			rewind();
		}
	}

	@Override
	protected String getTraceString(String status, int receiverCount) {
		return MessageFormat.format("{0} message '{1}' for command status '{2}' to {3} observer(s).", status, message.type(), commandStatus(),
				receiverCount);
	}

}
