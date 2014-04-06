package com.Antilias;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;

public class AMenuItem extends JMenuItem {
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
	

	public AMenuItem() {
		// TODO Auto-generated constructor stub
	}

	public AMenuItem(Icon icon) {
		super(icon);
		// TODO Auto-generated constructor stub
	}

	public AMenuItem(String text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	public AMenuItem(Action a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	public AMenuItem(String text, Icon icon) {
		super(text, icon);
		// TODO Auto-generated constructor stub
	}

	public AMenuItem(String text, int mnemonic) {
		super(text, mnemonic);
		// TODO Auto-generated constructor stub
	}

}
