package com.Antilias;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

public class AButton extends JButton {

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

	public AButton() {
		// TODO Auto-generated constructor stub
	}

	public AButton(Icon icon) {
		super(icon);
		// TODO Auto-generated constructor stub
	}

	public AButton(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public AButton(Action a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	public AButton(String text, Icon icon) {
		super(text, icon);
		// TODO Auto-generated constructor stub
	}

}
