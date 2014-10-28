package org.spicefactory.parsley.messaging.modelgen;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Generated;
import javax.annotation.processing.FilerException;
import javax.tools.FileObject;

import org.spicefactory.parsley.messaging.metamodel.MessageMetamodel;

import com.swissquote.eforex.fxbook.infrastructure.core.messaging.modelgen.model.MetaAttribute;
import com.swissquote.eforex.fxbook.infrastructure.core.messaging.modelgen.model.MetaMessage;

/**
 * Helper class to write the actual meta model class using the {@link javax.annotation.processing.Filer} API.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
public final class ClassWriter {
	private static final String META_MODEL_CLASS_NAME_SUFFIX = "_";
	private static final ThreadLocal<SimpleDateFormat> SIMPLE_DATE_FORMAT = new ThreadLocal<SimpleDateFormat>() {
		@Override
		public SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		}
	};

	private ClassWriter() {
	}

	/////////////////////////////////////////////////////////////////////////////
	// Package-private.
	/////////////////////////////////////////////////////////////////////////////

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	public static void writeFile(MetaMessage message, Context context) {
		try {
			String metaModelPackage = message.getPackageName();
			// Needs to generate body first, since this will also update the required
			// imports which need to be written out first.
			String body = generateBody(message, context).toString();

			FileObject fo =
					context.getProcessingEnvironment().getFiler().createSourceFile(getFullyQualifiedClassName(message, metaModelPackage));
			OutputStream os = fo.openOutputStream();
			PrintWriter pw = new PrintWriter(os);

			if (!metaModelPackage.isEmpty()) {
				pw.println("package " + metaModelPackage + ";");
				pw.println();
			}
			pw.println(message.generateImports());
			pw.println(body);

			pw.flush();
			pw.close();
		}
		catch (FilerException filerEx) {
			context.error("Problem with Filer: {}.", filerEx.getMessage());
		}
		catch (IOException ioEx) {
			context.error("Problem opening file to write MetaModel for {}: {}.", message.getSimpleName(), ioEx.getMessage());
		}
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	private static StringBuffer generateBody(MetaMessage message, Context context) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(sw);

			if (context.addGeneratedAnnotation()) {
				pw.println(writeGeneratedAnnotation(message, context));
			}

			if (context.addSuppressWarningsAnnotation()) {
				pw.println(writeSuppressWarnings());
			}

			pw.println(writeMessageMetamodelAnnotation(message));

			printClassDeclaration(message, pw, context);

			pw.println();

			List<MetaAttribute> members = message.getMembers();
			for (MetaAttribute metaMember : members) {
				pw.println("	" + metaMember.getDeclarationString());
			}

			pw.println();
			pw.println("}");
			return sw.getBuffer();
		}
		finally {
			if (pw != null) {
				pw.close();
			}
		}
	}

	private static void printClassDeclaration(MetaMessage message, PrintWriter pw, Context context) {
		pw.println("public abstract class " + message.getSimpleName() + META_MODEL_CLASS_NAME_SUFFIX + " {");
	}

	private static String getFullyQualifiedClassName(MetaMessage message, String metaModelPackage) {
		String fullyQualifiedClassName = "";
		if (!metaModelPackage.isEmpty()) {
			fullyQualifiedClassName = metaModelPackage + ".";
		}
		fullyQualifiedClassName = fullyQualifiedClassName + message.getSimpleName() + META_MODEL_CLASS_NAME_SUFFIX;
		return fullyQualifiedClassName;
	}

	private static String writeGeneratedAnnotation(MetaMessage message, Context context) {
		StringBuilder generatedAnnotation = new StringBuilder();
		generatedAnnotation.append("@").append(message.importType(Generated.class.getName())).append("(value = \"")
				.append(MessageMetamodelProcessor.class.getName());
		if (context.addGeneratedDate()) {
			generatedAnnotation.append("\", date = \"").append(SIMPLE_DATE_FORMAT.get().format(new Date())).append("\")");
		} else {
			generatedAnnotation.append("\")");
		}
		return generatedAnnotation.toString();
	}

	private static String writeSuppressWarnings() {
		return "@SuppressWarnings(\"all\")";
	}

	private static String writeMessageMetamodelAnnotation(MetaMessage message) {
		return "@" + message.importType(MessageMetamodel.class.getCanonicalName()) + "(" + message.getSimpleName() + ".class)";
	}
}
