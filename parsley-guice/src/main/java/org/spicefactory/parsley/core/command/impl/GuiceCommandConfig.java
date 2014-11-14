package org.spicefactory.parsley.core.command.impl;

import org.spicefactory.parsley.core.command.CommandManager;

import com.google.inject.AbstractModule;

public class GuiceCommandConfig extends AbstractModule {

	@Override
	protected void configure() {
		bind(CommandManager.class).to(GuiceCommandManager.class);
	}
}
