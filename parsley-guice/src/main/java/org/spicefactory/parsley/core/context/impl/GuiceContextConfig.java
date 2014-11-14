package org.spicefactory.parsley.core.context.impl;

import org.spicefactory.parsley.core.context.Context;

import com.google.inject.AbstractModule;

public class GuiceContextConfig extends AbstractModule {

	@Override
	protected void configure() {
		bind(Context.class).to(GuiceContext.class);
	}

}
