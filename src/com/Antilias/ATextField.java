/**
 * 
 */
package com.Antilias;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JTextField;
import javax.swing.text.Document;

/**
 * @author jyhong
 *
 */
public class ATextField extends JTextField {

	/**
	 * 抗锯齿化
	 */
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		super.paint(g2);
	}
	
	
	/**
	 * 
	 */
	public ATextField() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param text
	 */
	public ATextField(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param columns
	 */
	public ATextField(int columns) {
		super(columns);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param text
	 * @param columns
	 */
	public ATextField(String text, int columns) {
		super(text, columns);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param doc
	 * @param text
	 * @param columns
	 */
	public ATextField(Document doc, String text, int columns) {
		super(doc, text, columns);
		// TODO Auto-generated constructor stub
	}

}
