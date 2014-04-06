/**
 * 
 */
package com;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;

/**
 * @author jyhong
 *
 */
public class SplashWindow extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2651894737476119690L;
	
	ImageCanvas imgCanvas;
	int splashWidth = 0;
	int splashHeight = 0;

	/**
	 * @param owner
	 * @param splashImage
	 */
	public SplashWindow(Frame owner, Image splashImage) {
		super(owner);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		imgCanvas = new ImageCanvas(splashImage);
//			splashImage = ImageIO.read(new File("data/BaiduYunSplash.png"));
		int splashWidth = imgCanvas.getImageWidth();
		int splashHeight = imgCanvas.getImageHeight();
		this.add(imgCanvas);
		this.setBounds((int)screenSize.getWidth()/2 - splashWidth/2,
				(int)screenSize.getHeight()/2 - splashHeight/2,
				splashWidth,
				splashHeight);
		this.setAlwaysOnTop(false);// not work
		this.setVisible(true);
	}
	
	public void setText(String str) {
		this.imgCanvas.drawString(str, 100, 100);
	}

}
