package org.spicefactory.parsley.core.messaging;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Represents a meta-data tag that can be used to specify a property for a message that can be used as a selector. Selectors may refine the
 * message selection process for registered message handlers which per default are selected by message type only.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
@Documented
@Target(FIELD)
@Retention(RUNTIME)
public @interface Selector {

	/**
	 * Represents the default value when no selector has been set.
	 */
	// Java annotations restricts member type so we cannot define an Object,
	// we use an integer instead, just like swing events. This also have the
	// effect of being compatible with java.awt.AWTEvent type property.
	final static int NONE = 0x0;

}
