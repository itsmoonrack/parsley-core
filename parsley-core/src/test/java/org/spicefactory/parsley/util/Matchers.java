package org.spicefactory.parsley.util;

import org.spicefactory.parsley.util.matcher.ContextStateMatcher;

public class Matchers {

	public static ContextStateMatcher contextInState() {
		return contextInState(true, true, false);
	}

	public static ContextStateMatcher contextInState(boolean configured) {
		return contextInState(configured, true, false);
	}

	public static ContextStateMatcher contextInState(boolean configured, boolean initialized) {
		return contextInState(configured, initialized, false);
	}

	public static ContextStateMatcher contextInState(boolean configured, boolean initialized, boolean destroyed) {
		return new ContextStateMatcher(configured, initialized, destroyed);
	}
}
