package org.spicefactory.parsley.util.matcher;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.spicefactory.parsley.core.context.Context;

public class ContextStateMatcher extends BaseMatcher<Context> {

	private final boolean configured, initialized, destroyed;

	public ContextStateMatcher(boolean configured, boolean initialized, boolean destroyed) {
		this.configured = configured;
		this.initialized = initialized;
		this.destroyed = destroyed;
	}

	@Override
	public boolean matches(Object item) {
		if (!(item instanceof Context))
			return false;
		Context context = (Context) item;
		return context.isConfigured() == configured && context.isInitialized() == initialized && context.isDestroyed() == destroyed;
	}

	@Override
	public void describeTo(Description description) {
		description.appendValue("Context with state: configured=" + configured + ", initialized=" + initialized + ", destroyed=" + destroyed);
	}
}
