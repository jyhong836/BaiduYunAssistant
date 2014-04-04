/**
 * 
 */
package com;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author jyhong
 *
 */
public class HelpDialog extends JDialog {
//	private final String dataFolderString = new String("data/");

	/**
	 * 
	 */
	public HelpDialog() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param owner
	 */
	public HelpDialog(Frame owner) {
		super(owner);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param owner
	 */
	public HelpDialog(Dialog owner) {
		super(owner);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param owner
	 */
	public HelpDialog(Window owner) {
		super(owner);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param owner
	 * @param modal
	 */
	public HelpDialog(Frame owner, boolean modal) {
		super(owner, modal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param owner
	 * @param title
	 */
	public HelpDialog(Frame owner, String title) {
		super(owner, title);
		JTextArea helpTextArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(helpTextArea);
		helpTextArea.setLineWrap(true);
		File helpFile = new File(BaiduYunAssistant.dataFolderString+"help.dat");
//		StringBuffer bs = new StringBuffer();
		String bufString;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(helpFile)));
			while ((bufString = br.readLine())!=null){
				helpTextArea.append("\n"+bufString);
			}
		} catch (FileNotFoundException e) {
			helpTextArea.append("Cannot find help file, you can use 'bypy' to get the help");
		} catch (IOException e) {
			System.out.println("read file error");
		}
//		helpTextArea.setText(bs.toString());
		this.add(scrollPane);
		this.setVisible(false);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int framewidth = 600;
		int frameheight = 400;
		this.setBounds((int)screenSize.getWidth()/2 - framewidth/2,
				(int)screenSize.getHeight()/2 - frameheight/2,
				framewidth,
				frameheight);
	}

	/**
	 * @param owner
	 * @param modal
	 */
	public HelpDialog(Dialog owner, boolean modal) {
		super(owner, modal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param owner
	 * @param title
	 */
	public HelpDialog(Dialog owner, String title) {
		super(owner, title);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param owner
	 * @param modalityType
	 */
	public HelpDialog(Window owner, ModalityType modalityType) {
		super(owner, modalityType);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param owner
	 * @param title
	 */
	public HelpDialog(Window owner, String title) {
		super(owner, title);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 */
	public HelpDialog(Frame arg0, String arg1, boolean arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param owner
	 * @param title
	 * @param modal
	 */
	public HelpDialog(Dialog owner, String title, boolean modal) {
		super(owner, title, modal);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param owner
	 * @param title
	 * @param modalityType
	 */
	public HelpDialog(Window owner, String title, ModalityType modalityType) {
		super(owner, title, modalityType);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param arg3
	 */
	public HelpDialog(Frame arg0, String arg1, boolean arg2,
			GraphicsConfiguration arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param owner
	 * @param title
	 * @param modal
	 * @param gc
	 */
	public HelpDialog(Dialog owner, String title, boolean modal,
			GraphicsConfiguration gc) {
		super(owner, title, modal, gc);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param owner
	 * @param title
	 * @param modalityType
	 * @param gc
	 */
	public HelpDialog(Window owner, String title, ModalityType modalityType,
			GraphicsConfiguration gc) {
		super(owner, title, modalityType, gc);
		// TODO Auto-generated constructor stub
	}

}
