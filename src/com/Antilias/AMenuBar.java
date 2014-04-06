package com.Antilias;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JMenuBar;

public class AMenuBar extends JMenuBar {
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
	

	public AMenuBar() {
		// TODO Auto-generated constructor stub
	}

}
