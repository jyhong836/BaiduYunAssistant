package com.Antilias;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Action;
import javax.swing.JMenu;

public class AMenu extends JMenu {
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
	

	public AMenu() {
		// TODO Auto-generated constructor stub
	}

	public AMenu(String s) {
		super(s);
		// TODO Auto-generated constructor stub
	}

	public AMenu(Action a) {
		super(a);
		// TODO Auto-generated constructor stub
	}

	public AMenu(String s, boolean b) {
		super(s, b);
		// TODO Auto-generated constructor stub
	}

}
