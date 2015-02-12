package org.spicefactory.parsley.core.bootstrap.impl;

import org.spicefactory.parsley.core.messaging.MessageSettings;
import org.spicefactory.parsley.core.scope.Scope;
import org.spicefactory.parsley.core.scope.ScopeDefinition;
import org.spicefactory.parsley.core.view.ViewSettings;

import com.google.inject.AbstractModule;

@ViewSettings
@MessageSettings
@ScopeDefinition(name = Scope.GLOBAL)
public class GuiceBootstrapConfig extends AbstractModule {

	@Override
	protected void configure() {
		// TODO Auto-generated method stub

	}

}
