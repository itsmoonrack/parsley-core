package org.spicefactory.parsley.messaging.metamodel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The <code>MessageMetamodel</code> annotation specifies that the class is a meta-model class that represents the message, mapped superclass, or
 * embeddable class designated by the value element.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MessageMetamodel {
	/**
	 * Class being modeled by the annotated class.
	 */
	Class<?> value();
}
