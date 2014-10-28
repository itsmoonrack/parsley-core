package org.spicefactory.parsley.messaging.metamodel;

/**
 * Instances of the type <code>SingularAttribute</code> represents single-valued properties or fields.
 * @param <X> The type containing the represented attribute
 * @param <T> The type of the represented attribute
 */
public interface SingularAttribute<X, T> extends Attribute<X, T> {

	/**
	 * Can the attribute be null.
	 * @return boolean indicating whether the attribute can be null
	 */
	boolean isOptional();

	/**
	 * Return the type that represents the type of the attribute.
	 * @return type of attribute
	 */
	Type<T> getType();

}
