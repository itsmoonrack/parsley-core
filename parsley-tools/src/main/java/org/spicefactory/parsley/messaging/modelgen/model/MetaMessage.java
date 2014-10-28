package org.spicefactory.parsley.messaging.modelgen.model;

import java.util.List;

import javax.lang.model.element.TypeElement;

public interface MetaMessage extends ImportContext {

	String getPackageName();

	String getSimpleName();

	@Override
	String generateImports();

	@Override
	String importType(String fqcn);

	TypeElement getTypeElement();

	List<MetaAttribute> getMembers();

	boolean isMetaComplete();

}
