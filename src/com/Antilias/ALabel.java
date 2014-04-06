/**
 * 
 */
package com.Antilias;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * @author jyhong
 *
 */
public class ALabel extends JLabel {

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
	public ALabel() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param text
	 */
	public ALabel(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param image
	 */
	public ALabel(Icon image) {
		super(image);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param text
	 * @param horizontalAlignment
	 */
	public ALabel(String text, int horizontalAlignment) {
		super(text, horizontalAlignment);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param image
	 * @param horizontalAlignment
	 */
	public ALabel(Icon image, int horizontalAlignment) {
		super(image, horizontalAlignment);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param text
	 * @param icon
	 * @param horizontalAlignment
	 */
	public ALabel(String text, Icon icon, int horizontalAlignment) {
		super(text, icon, horizontalAlignment);
		// TODO Auto-generated constructor stub
	}

}
