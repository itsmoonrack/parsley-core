package org.spicefactory.parsley.messaging.modelgen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;

import org.spicefactory.parsley.messaging.modelgen.model.MetaMessage;

@SupportedAnnotationTypes({"org.spicefactory.parsley.messaging.annotation.MessageHandler"})
public class MessageMetamodelProcessor extends AbstractProcessor {

	public static final String ADD_GENERATION_DATE = "addGenerationDate";
	public static final String ADD_GENERATED_ANNOTATION = "addGeneratedAnnotation";
	public static final String ADD_SUPPRESS_WARNINGS_ANNOTATION = "addSuppressWarningsAnnotation";

	private Context context;

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		// TODO Auto-generated method stub
		return false;
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	private void createMetamodelClasses() {
		// Keeps track of all classes for which model have been generated.
		Collection<String> generatedModelClasses = new ArrayList<String>();

		for (MetaMessage message : context.getMetaMessages()) {
			context.info("Writing meta-model for message {}.", message);
		}

	}
}
