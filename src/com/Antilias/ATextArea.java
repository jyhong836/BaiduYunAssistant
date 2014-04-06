/**
 * 
 */
package com.Antilias;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JTextArea;
import javax.swing.text.Document;

/**
 * @author jyhong
 *
 */
public class ATextArea extends JTextArea {

	/**
	 * 
	 */
	public ATextArea() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param text
	 */
	public ATextArea(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param doc
	 */
	public ATextArea(Document doc) {
		super(doc);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param rows
	 * @param columns
	 */
	public ATextArea(int rows, int columns) {
		super(rows, columns);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param text
	 * @param rows
	 * @param columns
	 */
	public ATextArea(String text, int rows, int columns) {
		super(text, rows, columns);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param doc
	 * @param text
	 * @param rows
	 * @param columns
	 */
	public ATextArea(Document doc, String text, int rows, int columns) {
		super(doc, text, rows, columns);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		super.paint(g2);
	}

}
