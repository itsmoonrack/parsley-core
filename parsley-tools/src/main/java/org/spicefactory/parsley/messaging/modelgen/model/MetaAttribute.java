package org.spicefactory.parsley.messaging.modelgen.model;

public interface MetaAttribute {
	String getDeclarationString();

	String getMetaType();

	String getPropertyName();

	String getTypeDeclaration();

	MetaMessage getHostingMessage();
}
