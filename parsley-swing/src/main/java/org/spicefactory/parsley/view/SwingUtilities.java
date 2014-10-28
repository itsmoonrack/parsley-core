package org.spicefactory.parsley.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;

/**
 * A collection of utility methods for Swing.
 */
public class SwingUtilities {

	/**
	 * Returns the first <code>Window </code> ancestor of <code>c</code>, or {@code null} if <code>c</code> is not contained inside a
	 * <code>Window</code>.
	 * @param c <code>Component</code> to get <code>Window</code> ancestor of.
	 * @return the first <code>Window </code> ancestor of <code>c</code>, or {@code null} if <code>c</code> is not contained inside a
	 *         <code>Window</code>.
	 * @since 1.3
	 */
	public static Window getContextAncestor(Component c) {
		for (Container p = c.getParent(); p != null; p = p.getParent()) {
			if (p instanceof Window) {
				return (Window) p;
			}
		}
		return null;
	}
}
