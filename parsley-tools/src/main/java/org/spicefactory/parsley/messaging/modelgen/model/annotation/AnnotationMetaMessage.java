package org.spicefactory.parsley.messaging.modelgen.model.annotation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.ElementFilter;

import org.spicefactory.parsley.messaging.modelgen.Context;
import org.spicefactory.parsley.messaging.modelgen.ImportContextImpl;
import org.spicefactory.parsley.messaging.modelgen.model.ImportContext;
import org.spicefactory.parsley.messaging.modelgen.model.MetaAttribute;
import org.spicefactory.parsley.messaging.modelgen.model.MetaMessage;
import org.spicefactory.parsley.messaging.modelgen.util.AccessType;

public class AnnotationMetaMessage implements MetaMessage {

	private final ImportContext importContext;
	private final TypeElement element;
	private final Map<String, MetaAttribute> members;
	private Context context;

	//	private AccessTypeInformation entityAccessTypeInfo;

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	public AnnotationMetaMessage(TypeElement element, Context context) {
		this(element, context, false);
	}

	@Override
	public final String staticImport(String fqcn, String member) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final String getPackageName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final String getSimpleName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final String generateImports() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final String importType(String fqcn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public final TypeElement getTypeElement() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MetaAttribute> getMembers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isMetaComplete() {
		// TODO Auto-generated method stub
		return false;
	}

	public final String getQualifiedName() {
		// TODO Auto-generated method stub
		return null;
	}

	public final Context getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	protected AnnotationMetaMessage(TypeElement element, Context context, boolean lazilyInitialised) {
		this.element = element;
		this.context = context;
		this.members = new HashMap<String, MetaAttribute>();
		this.importContext = new ImportContextImpl(getPackageName());
		if (!lazilyInitialised) {
			init();
		}
	}

	protected final void init() {
		List<? extends Element> fieldsOfClass = ElementFilter.fieldsIn(element.getEnclosedElements());
		addMessageMembers(fieldsOfClass, AccessType.FIELD);

		List<? extends Element> methodsOfClass = ElementFilter.methodsIn(element.getEnclosedElements());
		addMessageMembers(methodsOfClass, AccessType.PROPERTY);
	}

	private void addMessageMembers(List<? extends Element> membersOfClass, AccessType membersKind) {
		for (Element memberOfClass : membersOfClass) {
			MetaAttributeGenerationVisitor visitor = new MetaAttributeGenerationVisitor(this, context);
			AnnotationMetaAttribute result = memberOfClass.asType().accept(visitor, memberOfClass);
		}
	}
}
