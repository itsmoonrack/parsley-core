package org.spicefactory.parsley.messaging.modelgen;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.swissquote.eforex.fxbook.infrastructure.core.messaging.modelgen.model.ImportContext;

public class ImportContextImpl implements ImportContext {

	private Set<String> imports = new TreeSet<String>();
	private Set<String> staticImports = new TreeSet<String>();
	private Map<String, String> simpleNames = new HashMap<String, String>();

	private String basePackage = "";

	private static final Map<String, String> PRIMITIVES = new HashMap<String, String>();

	static {
		PRIMITIVES.put("char", "Character");

		PRIMITIVES.put("byte", "Byte");
		PRIMITIVES.put("short", "Short");
		PRIMITIVES.put("int", "Integer");
		PRIMITIVES.put("long", "Long");

		PRIMITIVES.put("boolean", "Boolean");

		PRIMITIVES.put("float", "Float");
		PRIMITIVES.put("double", "Double");

	}

	/////////////////////////////////////////////////////////////////////////////
	// Public API.
	/////////////////////////////////////////////////////////////////////////////

	public ImportContextImpl(String basePackage) {
		this.basePackage = basePackage;
	}

	@Override
	public String importType(String fqcn) {
		String result = fqcn;

		String additionalTypePart = null;
		if (fqcn.indexOf('<') >= 0) {
			additionalTypePart = result.substring(fqcn.indexOf('<'));
			result = result.substring(0, fqcn.indexOf('<'));
			fqcn = result;
		} else if (fqcn.indexOf('[') >= 0) {
			additionalTypePart = result.substring(fqcn.indexOf('['));
			result = result.substring(0, fqcn.indexOf('['));
			fqcn = result;
		}

		String pureFqcn = fqcn.replace('$', '.');

		boolean canBeSimple;

		String simpleName = unqualify(fqcn);
		if (simpleNames.containsKey(simpleName)) {
			String existingFqcn = simpleNames.get(simpleName);
			if (existingFqcn.equals(pureFqcn)) {
				canBeSimple = true;
			} else {
				canBeSimple = false;
			}
		} else {
			canBeSimple = true;
			simpleNames.put(simpleName, pureFqcn);
			imports.add(pureFqcn);
		}

		if (inSamePackage(fqcn) || (imports.contains(pureFqcn) && canBeSimple)) {
			result = unqualify(result); // de-qualify
		} else if (inJavaLang(fqcn)) {
			result = result.substring("java.lang.".length());
		}

		if (additionalTypePart != null) {
			result = result + additionalTypePart;
		}

		result = result.replace('$', '.');
		return result;
	}

	@Override
	public String staticImport(String fqcn, String member) {
		String local = fqcn + "." + member;
		imports.add(local);
		staticImports.add(local);

		if (member.equals("*")) {
			return "";
		}
		return member;
	}

	@Override
	public String generateImports() {
		StringBuffer buf = new StringBuffer();

		for (String next : imports) {
			// don't add automatically "imported" stuff
			if (!isAutoImported(next)) {
				if (staticImports.contains(next)) {
					buf.append("import static ").append(next).append(";\r\n");
				} else {
					buf.append("import ").append(next).append(";\r\n");
				}
			}
		}

		if (buf.indexOf("$") >= 0) {
			return buf.toString();
		}
		return buf.toString();
	}

	/////////////////////////////////////////////////////////////////////////////
	// Internal implementation.
	/////////////////////////////////////////////////////////////////////////////

	public static String unqualify(String qualifiedName) {
		int loc = qualifiedName.lastIndexOf('.');
		return (loc < 0) ? qualifiedName : qualifiedName.substring(qualifiedName.lastIndexOf('.') + 1);
	}

	public static String qualifier(String qualifiedName) {
		int loc = qualifiedName.lastIndexOf(".");
		return (loc < 0) ? "" : qualifiedName.substring(0, loc);
	}

	private boolean isPrimitive(String className) {
		return PRIMITIVES.containsKey(className);
	}

	private boolean isAutoImported(String next) {
		return isPrimitive(next) || inDefaultPackage(next) || inJavaLang(next) || inSamePackage(next);
	}

	private boolean inDefaultPackage(String className) {
		return className.indexOf(".") < 0;
	}

	private boolean inSamePackage(String className) {
		String other = qualifier(className);
		return other == basePackage || (other != null && other.equals(basePackage));
	}

	private boolean inJavaLang(String className) {
		return "java.lang".equals(qualifier(className));
	}
}
