package com.Antilias;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BoundedRangeModel;
import javax.swing.JProgressBar;

public class AProgressBar extends JProgressBar {

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

	public AProgressBar() {
		// TODO Auto-generated constructor stub
	}

	public AProgressBar(int orient) {
		super(orient);
		// TODO Auto-generated constructor stub
	}

	public AProgressBar(BoundedRangeModel newModel) {
		super(newModel);
		// TODO Auto-generated constructor stub
	}

	public AProgressBar(int min, int max) {
		super(min, max);
		// TODO Auto-generated constructor stub
	}

	public AProgressBar(int orient, int min, int max) {
		super(orient, min, max);
		// TODO Auto-generated constructor stub
	}

}
