package org.spicefactory.parsley.messaging.metamodel;

/**
 * Represents an attribute of a Java type.
 * @param <X> The represented type that contains the attribute
 * @param <Y> The type of the represented attribute
 */
public interface Attribute<X, Y> {

	/**
	 * Return the name of the attribute.
	 * @return name
	 */
	String getName();

	/**
	 * Return the managed type representing the type in which the attribute was declared.
	 * @return declaring type
	 */
	ManagedType<X> getDeclaringType();

	/**
	 * Return the Java type of the represented attribute.
	 * @return Java type
	 */
	Class<Y> getJavaType();

	/**
	 * Is the attribute collection-valued (represents a Collection, Set, List, or Map).
	 * @return boolean indicating whether the attribute is collection-valued
	 */
	boolean isCollection();

}
