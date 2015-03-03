package org.spicefactory.parsley.core.messaging.impl;

import java.text.MessageFormat;
import java.util.List;

import org.spicefactory.parsley.core.command.CommandObserverProcessor;
import org.spicefactory.parsley.core.command.CommandStatus;
import org.spicefactory.parsley.core.command.ObservableCommand;
import org.spicefactory.parsley.core.messaging.MessageProcessor;
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
		super(observable.getTrigger(), triggerCache, settings, invokeReceiver);

		this.typeCache = typeCache;
		this.observable = observable;
		this.status = observable.getStatus();
		this.result = observable.getResult();
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	@Override
	public Object getCommand() {
		return observable.getCommand();
	}

	@Override
	public Object getResult() {
		return result;
	}

	@Override
	public CommandStatus getCommandStatus() {
		return status;
	}

	@Override
	public boolean isRoot() {
		return observable.isRoot();
	}

	@Override
	public void changeResult(Object result, boolean error) {
		if (getCommandStatus() == CommandStatus.EXECUTE) {
			throw new IllegalStateException("Cannot set the result while command is still executing.");
		}
		this.result = result;
		this.status = (error) ? CommandStatus.EXCEPTION : CommandStatus.COMPLETE;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	@Override
	protected List<MessageReceiver> fetchReceivers() {
		List<MessageReceiver> receivers =
				typeCache.getReceivers(MessageReceiverKind.forCommandStatus(getCommandStatus(), false), observable.getId());

		if (observable.getTrigger() != null) {
			receivers.addAll(cache.getReceivers(MessageReceiverKind.forCommandStatus(getCommandStatus(), true), observable.getTrigger()
					.getSelector()));
		}

		return receivers;
	}

	// Java 1.8 forward-compatibility.
	private static final Handler invokeReceiver = new Handler() {
		@Override
		public void proceed(MessageProcessor<Object> processor, MessageReceiver receiver) {
			invokeReceiver((DefaultCommandObserverProcessor) processor, (CommandObserver) receiver);
		}
	};

	private static void invokeReceiver(DefaultCommandObserverProcessor processor, CommandObserver observer) {
		CommandStatus oldStatus = processor.getCommandStatus();
		observer.observeCommand(processor);
		if (oldStatus != processor.getCommandStatus() && oldStatus != CommandStatus.EXECUTE) {
			processor.rewind();
		}
	}


	@Override
	protected String getTraceString(String status, int receiverCount) {
		return MessageFormat.format("{0} message ''{1}'' for command status ''{2}'' to {3} observer(s).", status, message.getType()
				.getSimpleName(), getCommandStatus(), receiverCount);
	}

}
