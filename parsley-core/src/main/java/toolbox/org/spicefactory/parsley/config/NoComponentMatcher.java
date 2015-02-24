package org.spicefactory.parsley.config;

import java.awt.Component;

import com.google.inject.TypeLiteral;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;

/**
 * Creates a matcher that does not select subclasses of java.awt.Component.
 * <p>
 * The primary goal is to avoid reflecting on giant classes such as javax.swing.JComponent which would use a lot of CPU cycles when injection is
 * seldom if never used on these classes. Best practices are to use a Presentation Model for the state of the UI, and inject them in swing
 * component by Guice mechanism which does not reflect on parent classes.
 * <p>
 * Use this matcher when you want to reflect on parent classes such as base or abstract classes.
 * @author Sylvain Lecoy <sylvain.lecoy@gmail.com>
 */
@SuppressWarnings("rawtypes")
public class NoComponentMatcher extends AbstractMatcher<TypeLiteral> {

	private final Matcher<Class> matcher = Matchers.subclassesOf(Component.class);
	private final Matcher<Class> matcherNot = Matchers.not(matcher);

	@Override
	public boolean matches(TypeLiteral t) {
		return matcherNot.matches(t.getRawType());
	}

}
