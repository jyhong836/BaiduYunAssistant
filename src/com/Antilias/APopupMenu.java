package com.Antilias;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPopupMenu;

public class APopupMenu extends JPopupMenu {
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

	public APopupMenu() {
		// TODO Auto-generated constructor stub
	}

	public APopupMenu(String label) {
		super(label);
		// TODO Auto-generated constructor stub
	}

}
