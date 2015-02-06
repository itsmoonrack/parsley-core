package org.spicefactory.parsley.command.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * A command is seen as asynchronous by the framework when it is annotated by @Async tag.
 * <p>
 * When a command is asynchronous, the execute method is generally called in a background thread, depending on the implementation.
 * @author Sylvain Lecoy <sylvain.lecoy@swissquote.ch>
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface Async {

}
