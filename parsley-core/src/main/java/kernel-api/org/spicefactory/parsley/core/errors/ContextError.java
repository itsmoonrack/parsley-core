package org.spicefactory.parsley.core.errors;

/**
 * Error thrown by <code>Context</code> implementations.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
public class ContextError extends Error {

	public ContextError(String message) {
		super(message);
	}

	public ContextError(String message, Throwable cause) {
		super(message, cause);
	}

}
