package org.spicefactory.parsley.view;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class FastInjectTest extends JFrame {

	private FastInjectTest() {

	}

	void test() {
		//		SwingUtilities.getWindowAncestor(c)
		Component c = getParent();
		while (c != null) {
			System.out.println(c);
			c = c.getParent();
		}
	}

	public static void main(String[] args) throws InterruptedException, InvocationTargetException {
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				FastInjectTest t = new FastInjectTest();
				t.setTitle("test");
				t.setSize(300, 200);
				t.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				t.setVisible(true);
				t.test();
			}
		});
	}

}
