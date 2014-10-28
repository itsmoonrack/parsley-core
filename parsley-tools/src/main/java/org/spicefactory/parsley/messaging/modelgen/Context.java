package org.spicefactory.parsley.messaging.modelgen;

import java.util.Collection;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.util.Elements;

import org.slf4j.Logger;

import com.swissquote.eforex.fxbook.infrastructure.core.messaging.modelgen.model.MetaMessage;

public final class Context {

	private Logger logger;

	private final ProcessingEnvironment pe;

	private boolean addGeneratedAnnotation = true;
	private boolean addGenerationDate;
	private boolean addSuppressWarningsAnnotation;

	public Context(ProcessingEnvironment pe) {
		this.pe = pe;
	}

	public Collection<MetaMessage> getMetaMessages() {
		// TODO Auto-generated method stub
		return null;
	}

	public ProcessingEnvironment getProcessingEnvironment() {
		return pe;
	}

	public boolean addGeneratedAnnotation() {
		return addGeneratedAnnotation;
	}

	public void setAddGeneratedAnnotation(boolean addGeneratedAnnotation) {
		this.addGeneratedAnnotation = addGeneratedAnnotation;
	}

	public boolean addGeneratedDate() {
		return addGenerationDate;
	}

	public void setAddGenerationDate(boolean addGenerationDate) {
		this.addGenerationDate = addGenerationDate;
	}

	public boolean addSuppressWarningsAnnotation() {
		return addSuppressWarningsAnnotation;
	}

	public void setAddSuppressWarningsAnnotation(boolean addSuppressWarningsAnnotation) {
		this.addSuppressWarningsAnnotation = addSuppressWarningsAnnotation;
	}

	public Elements getElementUtils() {
		return pe.getElementUtils();
	}

	public void info(String format, Object... arguments) {
		logger.info(format, arguments);
	}

	public void error(String format, Object... arguments) {
		logger.error(format, arguments);
	}

	public boolean containsMetaEntity(String fqcn) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean containsMetaEmbeddable(String fqcn) {
		// TODO Auto-generated method stub
		return false;
	}

}
