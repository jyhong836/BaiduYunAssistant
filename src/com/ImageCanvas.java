/**
 * 
 */
package com;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author jyhong
 *
 */
public class ImageCanvas extends Canvas {
	private Image img;
//	Dimension screenSize;
	private String string;
	private int imgWith = 100;
	private int stringY = 220;

	/**
	 * 
	 */
	public ImageCanvas(Image img) {
		this.img = img;
//		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		imgWith = img.getWidth(this);
	}

	/**
	 * @param config
	 */
	public ImageCanvas(GraphicsConfiguration config) {
		super(config);
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(img, 0, 0, this);
		g.drawString(string,
				(int)(imgWith/2-string.length()*7/2),
				stringY);
	}
	
	public int getImageWidth() {
		return img.getWidth(this);
	}
	
	public int getImageHeight() {
		return img.getHeight(this);
	}
	
	public void drawString(String str, int x, int y) {
		this.string = str;
		this.repaint();
	}

}