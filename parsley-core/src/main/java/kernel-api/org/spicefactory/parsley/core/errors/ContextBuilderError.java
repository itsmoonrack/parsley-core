package org.spicefactory.parsley.core.errors;

import java.util.List;

public class ContextBuilderError extends Error {

	private final List<Throwable> causes;

	public ContextBuilderError(String message, List<Throwable> causes) {
		super(message);
		this.causes = causes;
	}

}
