package org.spicefactory.parsley.messaging.metamodel;

/**
 * Instances of the type <code>Type</code> represent persistent object or attribute types.
 * @param <X> The type of the represented object or attribute
 */
public interface Type<X> {

	/**
	 * Return the represented Java type.
	 * @return Java type
	 */
	Class<X> getJavaType();

}
