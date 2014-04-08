package com.Antilias;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JCheckBox;

public class ACheckBox extends JCheckBox {

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

	public ACheckBox() {
		// TODO Auto-generated constructor stub
	}

	public ACheckBox(Icon icon) {
		super(icon);
		// TODO Auto-generated constructor stub
	}

	public ACheckBox(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public ACheckBox(Action a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	public ACheckBox(Icon icon, boolean selected) {
		super(icon, selected);
		// TODO Auto-generated constructor stub
	}

	public ACheckBox(String text, boolean selected) {
		super(text, selected);
		// TODO Auto-generated constructor stub
	}

	public ACheckBox(String text, Icon icon) {
		super(text, icon);
		// TODO Auto-generated constructor stub
	}

	public ACheckBox(String text, Icon icon, boolean selected) {
		super(text, icon, selected);
		// TODO Auto-generated constructor stub
	}

}
