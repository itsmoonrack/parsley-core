package org.spicefactory.parsley.messaging.modelgen.model.annotation;

import javax.lang.model.element.Element;

import org.spicefactory.parsley.messaging.modelgen.model.MetaSingleAttribute;

public class AnnotationMetaSingleAttribute extends AnnotationMetaAttribute implements MetaSingleAttribute {

	public AnnotationMetaSingleAttribute(AnnotationMetaMessage parent, Element element, String type) {
		super(element, parent, type);
	}

	@Override
	public String getMetaType() {
		return "com.swissquote.eforex.fxbook.infrastructure.core.messaging.metamodel.SingularAttribute";
	}
}
