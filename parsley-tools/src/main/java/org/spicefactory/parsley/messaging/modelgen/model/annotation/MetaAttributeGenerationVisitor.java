package org.spicefactory.parsley.messaging.modelgen.model.annotation;

import javax.lang.model.element.Element;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.SimpleTypeVisitor6;

import org.spicefactory.parsley.messaging.modelgen.Context;
import org.spicefactory.parsley.messaging.modelgen.util.TypeUtils;

public class MetaAttributeGenerationVisitor extends SimpleTypeVisitor6<AnnotationMetaAttribute, Element> {

	private final AnnotationMetaMessage message;
	private final Context context;

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	MetaAttributeGenerationVisitor(AnnotationMetaMessage message, Context context) {
		this.message = message;
		this.context = context;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	@Override
	public AnnotationMetaAttribute visitPrimitive(PrimitiveType t, Element element) {
		return new AnnotationMetaSingleAttribute(message, element, TypeUtils.toTypeString(t));
	}

	@Override
	public AnnotationMetaAttribute visitTypeVariable(TypeVariable t, Element element) {
		return new AnnotationMetaSingleAttribute(message, element, TypeUtils.toTypeString(t));
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////
}
