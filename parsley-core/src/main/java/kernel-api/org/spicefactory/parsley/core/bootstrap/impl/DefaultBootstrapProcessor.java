package org.spicefactory.parsley.core.bootstrap.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spicefactory.parsley.core.bootstrap.BootstrapInfo;
import org.spicefactory.parsley.core.bootstrap.BootstrapProcessor;
import org.spicefactory.parsley.core.bootstrap.ConfigurationProcessor;
import org.spicefactory.parsley.core.context.Context;
import org.spicefactory.parsley.core.context.ContextListener;
import org.spicefactory.parsley.core.errors.ContextBuilderError;
import org.spicefactory.parsley.core.events.ContextEvent;

public class DefaultBootstrapProcessor implements BootstrapProcessor {

	private ConfigurationProcessor currentProcessor;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final BootstrapInfo info;
	private final List<Throwable> errors;
	private final List<ConfigurationProcessor> processors;

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	public DefaultBootstrapProcessor() {
		errors = new ArrayList<Throwable>();
		processors = new ArrayList<ConfigurationProcessor>();
		info = null;
	}

	@Override
	public void addProcessor(ConfigurationProcessor processor) {
		processors.add(processor);
	}

	@Override
	public Context process() {
		info.getContext().addEventListener(ContextEvent.DESTROYED, contextDestroyed);
		invokeNextProcessor();
		return info.getContext();
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	private void invokeNextProcessor() {
		if (processors.size() == 0) {
			info.getContext().removeEventListener(ContextEvent.DESTROYED, contextDestroyed);
			if (errors.size() > 0) {
				handleError();
			}
		} else {
			ConfigurationProcessor processor = null;
			try {
				processor = processors.remove(0);
				handleProcessor(processor);
			}
			catch (Exception e) {
				removeCurrentProcessor();
				logger.error("Error processing {}: {}", processor, e);
				errors.add(e);
			}
			invokeNextProcessor();
		}
	}

	private void handleProcessor(ConfigurationProcessor processor) {
		//		processor.processConfiguration(info.getContext());
	}

	private void handleError() {
		throw new ContextBuilderError("One or more errors in BootstrapProcessor", errors);
	}

	private void removeCurrentProcessor() {
		if (currentProcessor == null)
			return;
		currentProcessor = null;
	}

	// Java 1.8 forward-compatibility.
	private final ContextListener contextDestroyed = new ContextListener() {
		@Override
		public void process(ContextEvent event) {
			contextDestroyed(event);
		}
	};

	private void contextDestroyed(ContextEvent event) {
		info.getContext().removeEventListener(ContextEvent.DESTROYED, contextDestroyed);
		//		if (currentProcessor != null)
		//			currentProcessor.cancel();
	}
}
