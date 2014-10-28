package org.spicefactory.parsley.messaging.modelgen.model.annotation;

import java.beans.Introspector;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.util.Elements;

import org.spicefactory.parsley.messaging.modelgen.model.MetaAttribute;
import org.spicefactory.parsley.messaging.modelgen.model.MetaMessage;

public abstract class AnnotationMetaAttribute implements MetaAttribute {

	private final Element element;
	private final AnnotationMetaMessage parent;
	private final String type;

	public AnnotationMetaAttribute(Element element, AnnotationMetaMessage parent, String type) {
		this.element = element;
		this.parent = parent;
		this.type = type;
	}

	@Override
	public String getDeclarationString() {
		return new StringBuilder().append("public static final String ") //
				.append(parent.importType(getMetaType())) //
				.append(getPropertyName()) //
				.append(" = \"") //
				.append(getPropertyName()) //
				.append("\";") //
				.toString();
	}

	@Override
	public String getPropertyName() {
		Elements elementsUtil = parent.getContext().getElementUtils();
		if (element.getKind() == ElementKind.FIELD) {
			return element.getSimpleName().toString();
		} else if (element.getKind() == ElementKind.METHOD) {
			String name = element.getSimpleName().toString();
			if (name.startsWith("get")) {
				return elementsUtil.getName(Introspector.decapitalize(name.substring("get".length()))).toString();
			} else if (name.startsWith("is")) {
				return elementsUtil.getName(Introspector.decapitalize(name.substring("is".length()))).toString();
			}
			return elementsUtil.getName(Introspector.decapitalize(name)).toString();
		} else {
			return elementsUtil.getName(element.getSimpleName() + "/* " + element.getKind() + " */").toString();
		}
	}

	@Override
	public String getTypeDeclaration() {
		return type;
	}

	@Override
	public MetaMessage getHostingMessage() {
		return parent;
	}
}
