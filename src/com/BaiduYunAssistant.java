package com;


import javax.imageio.ImageIO;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLayer;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.Timer;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
//import java.awt.Frame;
//import java.awt.Graphics;
//import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.JOptionPane;
import javax.xml.ws.Action;

import com.Antilias.*;

/**
 * 
 * @author jyhong
 * @version 1.00
 * created with Eclipse Kepler
 * JRE version:1.7
 * OS: Linux2.6, Ubuntu10.04.3
 * 
 * TODO:还不支持批量选择上传和下载
 * TODO:刷新之后下载/更新云端文件目录到本地
 * TODO：sync，先检查:syncup会自动覆盖云端内容旧的文件，不会上传相同文件
 * TODO:如果出故障了，应当有功能按键允许用户重新新建一个JFrame
 *
 */
public class BaiduYunAssistant 
				extends JFrame 
				implements ActionListener, 
							KeyListener,
							WindowListener, 
							MouseListener,
							Serializable, 
							FocusListener {
	/**
	 * @serial
	 * serialVersionUID的作用是当修改了class的程序之后，把早期版本保存的同名类
	 * 用ObjectReader读入时，会根据serialVersionUID是否相同来判断是否要进行读
	 * 取或称为反序列化(deserialize)。
	 * 如果serialVersionUID不同会发生这样的错误
	 * java.io.InvalidClassException: com.BaiduYunAssistant;
	 * local class incompatible: stream classdesc serialVersionUID = 1000,
	 *  local class serialVersionUID = 10001
	 *  
	 *  这里serialVersionUID设置成让eclipse自动生成了，每次修改之后eclipse都
	 *  会更新UID，这里的修改仅限于类函数和变量的增减，内容的改变不会起作用
	 */
	private static final long serialVersionUID = 1L;//getUID();//System.currentTimeMillis();//10001L;
	/**
	 * generate UID specific for access token
	 * @return 
	 */
//	private static long getUID() {
//		char[] tokenString = checkTokenFile().toCharArray();
//		long UID = 0;
//		for (char c:tokenString) {
//			UID += (long)c;
//		}
//		UID += 10001010010001L;
//		return UID;
//	}
	
	/* Layout and Container */
	private GridBagLayout mainLayout;
	private JPanel mainPanel;
	private LoadingLayer loadingLayerUI;
	
	private Container rightContainer;
	private GridBagLayout rightMainLayout;
	private Container leftContainer;
	private GridBagLayout leftMainLayout;
	
	private GridBagConstraints gbc;
	
	/*---------Left Grid Layout-----------*/
	/* Menu */
	private AMenuBar menuBar;
	//--help--
	private AMenu helpMenu;
	private AMenuItem aboutItem;
	private AMenuItem cmdhelpItem;
	private final HelpDialog helpDialog = new HelpDialog(this, "Command Help");
	private AMenu optionMenu;
	private AMenuItem settingsItem;
	
	/* component */
	private JPanel jp1;
	private ALabel cmdJLabel;
	private ATextField cmdfField;
	private ALabel cmdoutputLabel;
	private JTextArea cmdoutputArea;
	private JScrollPane cmdoutputJScrollPane;
	private ALabel searchLabel;
	private ATextField searchField;
	private ALabel pwdLabel;
	private ALabel pwdTextLabel;
	private DefaultTableModel tableModel;
	private ALabel jl_lb;
	private ATable fileListTable;
	private JScrollPane jlJScrollPane;
	private ALabel tokenTextField_lb;
	private ATextField tokenTextField;
	private AButton tokenRefreshButton;
	private ALabel spaceJLabel;
	private AProgressBar spaceBar;
	
	/* Control Buttons */
	private AButton refreshButton;
	private AButton homeButton;
	private AButton uploadButton;
	private AButton downloadButton;
	private AButton newDirButton;
	private AButton deleteButton;
	private AButton syncButton;
//	private AButton searchButton;
	/*---------END:Left Grid Layout-----------*/
	
	/*----------Right Grid Layout---------*/
	private ALabel taskLabel;
	private ATable taskTable;
	private DefaultTableModel taskTableModel;
	private JScrollPane taskScrollPane;
	/*-------END:Right Grid Layout--------*/
	
	/* parameters */
	public final static String dataFolderString = new String("data/");
	private ArrayBlockingQueue<String> cmdBuf;
	//-----------------------
	protected String bypyArgument = "bypy ";// argument shoud be add after this
	private String pwd = "/"; // currunt pwd
	private double cloudSpace = 0;
	private double usedSpace = 0;
	private Vector<RunCommandThread> taskVector;
	//-----------------------
	protected Vector<String> syncFiles;
	protected Vector<String> remoteSyncFiles;
	private JTree fileTree;
	private DataPackage datapackage;
	
//	private Image splashImage;
	private int framewidth = 900;
	private int frameheight = 600;

	public BaiduYunAssistant(DataPackage datapackage) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		//----------splash----------
		//TODO:--Window这个类满有意思的可以研究一下----
		SplashWindow splashWindow;
		try {
			splashWindow = new SplashWindow(this, ImageIO.read(new File("data/BaiduYunSplash.png")));
		} catch (IOException e1) {
			System.out.println("Cannot read splash file");
			splashWindow = null;
		}
		splashWindow.setText("Copyright 2014 Junyuan Hong");
		//----------END:splash--------
		
		//--------check access token--------
		splashWindow.setText("check for token access...");
		this.checkTokenFile();
		splashWindow.setText("check for token access...success");

		
		
		//---------init JFrame---------
		//----import data---
		if(datapackage!=null)
		{
			this.datapackage = datapackage;
			this.bypyArgument = datapackage.bypyArgument;
			this.pwd = datapackage.pwd;
			this.cloudSpace = datapackage.cloudSpace;
			this.usedSpace = datapackage.usedSpace;
			this.taskVector = datapackage.taskVector;
			this.fileTree = datapackage.fileTree;
			this.syncFiles = datapackage.syncFiles;
			this.remoteSyncFiles = datapackage.remoteSyncFiles;
			datapackage.checkData();
			System.out.println("import data success");
		} else {
			//-----no data, then init new parameters-------
			syncFiles = new Vector<String>();
			remoteSyncFiles = new Vector<String>();
			taskVector = new Vector<RunCommandThread>();
		}
		
		this.setBounds((int)screenSize.getWidth()/2 - framewidth/2,
				(int)screenSize.getHeight()/2 - frameheight/2,
				framewidth,
				frameheight);
		this.setTitle("Baidu Yun Assistant");
		Image BaiduYunIcon = Toolkit.getDefaultToolkit().getImage("data/BaiduYun.png");;
		this.setIconImage(BaiduYunIcon);
		//--------------END:init JFrame------------------
		
		//----------setLayout-------------
		mainLayout = new GridBagLayout();
		leftMainLayout = new GridBagLayout();
		rightMainLayout = new GridBagLayout();

		//--------set LayerUI-----
		mainPanel = new JPanel();
		mainPanel.setLayout(mainLayout);
		loadingLayerUI = new LoadingLayer();
		JLayer<JPanel> jLayer = new JLayer<JPanel>(mainPanel, loadingLayerUI);
		this.setLayout(new BorderLayout());
		this.add(jLayer);

		//----------init GridBagConstraints---------
		gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;// this not work
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 2;
//		gbc.gridheight = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		
		//----------init left and right containers----
		//LEFT
		leftContainer = new Container();
		mainPanel.add(leftContainer);
		leftContainer.setLayout(leftMainLayout);
		mainLayout.setConstraints(leftContainer, gbc);
		//RIGHT
		gbc.weightx = 5;
		rightContainer = new Container();
		mainPanel.add(rightContainer);
		rightContainer.setLayout(rightMainLayout);
		mainLayout.setConstraints(rightContainer, gbc);
		//------------------------------------
		
		
		//---------init component-----------
		splashWindow.setText("init component...");
		//---------Menu----------
		this.initMenuBar();

		//-------------Shell Command------------
		this.initCommand();
		
		//------------Shell Command Output------------
		this.initShellCommandOutput();
		
		//--------------init search bottons---------
		this.initSearchField();
		
		//-------------init pwd componets---------
		this.initPwdComponents();
		
		//---------------Table---------------
		this.initFileTable();
		
		//---------PCS Token----------
		this.initAccessToken();
		
		//---------------Space label---------------
		this.initSpaceBar();
		
		
		//-------------ADD Bottom Buttons------------
		this.initBottomButtons();
		
		//------------Task table--------------
		this.initTaskTable();
		
		//-----------gap------
		jp1 = new JPanel();
		leftContainer.add(jp1);
		gbc.gridwidth = 0;
		leftMainLayout.setConstraints(jp1, gbc);

		splashWindow.setText("start");
		
		//-------init parameters--------
		this.initParameters();
		
		//----------Command init----------------
//		try {
//			this.runCommand("");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		//-----------Frame settings--------------
		this.addWindowListener(this);
//		this.setResizable(false);
		if (splashWindow!=null)
			splashWindow.setVisible(false);
		this.setVisible(true);
	}

	 public static void setUIFont(FontUIResource f) {
		 //
		 // sets the default font for all Swing components.
		 // ex.
		 // setUIFont (new javax.swing.plaf.FontUIResource("Serif",Font.ITALIC,12));
		 //
		 java.util.Enumeration keys = UIManager.getDefaults().keys();
		 while (keys.hasMoreElements()) {
		 Object key = keys.nextElement();
		 Object value = UIManager.get(key);
		 if (value instanceof javax.swing.plaf.FontUIResource)
		 UIManager.put(key, f);
		 }
	 }
		 

	public static void main(String[] args) {
		/**
		 * print the Installed Look and feels
		 */
//		for (UIManager.LookAndFeelInfo info:UIManager.getInstalledLookAndFeels()) {
//			System.out.println(info);
//		}
		//---------check System----------
		System.out.println(
				UIManager.getCrossPlatformLookAndFeelClassName()+"\n"
				+UIManager.getSystemLookAndFeelClassName());
		Properties props=System.getProperties(); //获得系统属性集   
		String osName = props.getProperty("os.name"); //操作系统名称    
		String osArch = props.getProperty("os.arch"); //操作系统构架    
		String osVersion = props.getProperty("os.version"); //操作系统版本 
		String jreVersion = props.getProperty("java.specification.version");
		System.out.println(osName+" "+osArch+" "+osVersion);
		if (!osName.equals("Linux")) {
			System.out.println("Not a Linux System, exit");
			return;
		}
		if (Double.valueOf(jreVersion)<1.7) {
			System.out.println("WARNING:the JRE version("
					+jreVersion
					+") is lower than 1.7");
			return;
		}
		//-----------UIManage------------
		try {
			try {
				
				UIManager.setLookAndFeel(
	//					com.sun.java.swing.plaf.nimbus
	//					"com.sun.java.swing.plaf.nimbus.NumbusLookAndFeel");
	//					UIManager.getSystemLookAndFeelClassName());
//						"org.jb2011.lnf.windows2.Windows2LookAndFeel");
						"com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
	//					UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (ClassNotFoundException e1) {
				System.out.println("You have not install Nimbus Theme, use System Theme");
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (InstantiationException e1) {
				System.out.println("Error occur when import Numbus Theme, you may try to report it");
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (IllegalAccessException e1) {
				System.out.println("Error illegal access for Nimbus Theme");
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (UnsupportedLookAndFeelException e1) {
				System.out.println("Sorry, your System not support the Nimbus Theme");
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} 
//			setUIFont(new FontUIResource(Font.SANS_SERIF, Font.PLAIN, 14));
			
			GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			// GraphicsEnvironment是一个抽象类，不能实例化，只能用其中的静态方法获取一个实例
			String fontNames[];
			fontNames = environment.getAvailableFontFamilyNames();// 获取系统字体
//			for (String tmp:fontNames) {
//				System.out.println("System:"+tmp);
//			}
			
			
//			Font font = new Font(Font.SERIF,Font.PLAIN,15);
//	        UIManager.put("Button.font", font); 
//	        UIManager.put("Menu.font", font);
//	        UIManager.put("MenuItem.font", font);
//		    UIManager.put("Button.font", new Font("宋体", Font.BOLD , 32) ); 
		} catch(Exception e) {
			System.out.println("some unknown error occurred, you may report it");
		}
		//---------END:UIManage-----------
		
		
		System.out.println("Thank you for using Baidu Yun Assistant");
		System.out.println("Coyright 2014 Junyuan Hong ( GitHub: jyhong836 )");
		System.out.println("LICENSE GPLv3");
		System.out.println("NOTE: you need to add the bypy.py (https://github.com/houtianze/bypy) to /usr/bin/bypy first");
		File byaFile = new File(BaiduYunAssistant.dataFolderString+"BYA.dat");
		ObjectInputStream ois;
		DataPackage data;
		try {
			ois = new ObjectInputStream(new FileInputStream(byaFile));
			data = (DataPackage)ois.readObject();
			new BaiduYunAssistant(data);
			ois.close();
		} catch (FileNotFoundException e) {
			System.out.println("No saved data");
			new BaiduYunAssistant(null);
		} catch (IOException e) {
			System.out.println("数据类型不兼容，可能使用了旧版本的数据，删除旧数据");
			byaFile.delete();
			System.out.print(e.toString());
			new BaiduYunAssistant(null);
//			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("There is No BaiduYunAssistant DataPackage in BYA.dat");
			byaFile.delete();
			System.out.print(e.toString());
			new BaiduYunAssistant(null);
//			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source.equals(cmdfField)) {
			/* run commands */
			String cmd = cmdfField.getText();
			cmdfField.setText(null);
			try{
				runCommand(cmd);
			} catch (IOException e1) {
				this.cmdoutputArea.setText(cmd);
				this.cmdoutputArea.append("\nERROR:"+e1);
				e1.printStackTrace();
			}
		}
		else if(source.equals(refreshButton))
		{
        	loadingLayerUI.start(); 
        	this.setEnabled(false);
        	new Thread(new RunCommandThread(this, "quota", true, null){
        		@Override
        		public void extTask() {
        			BaiduYunAssistant.this.spaceBar.setValue((int)(BaiduYunAssistant.this.usedSpace/BaiduYunAssistant.this.cloudSpace*1000));
        			BaiduYunAssistant.this.spaceBar.setString(usedSpace+"GB/"+cloudSpace/1024+"TB "+
    						Math.floor(BaiduYunAssistant.this.usedSpace/BaiduYunAssistant.this.cloudSpace*1000)/10+"%");
        		}
        	}).start();
		}
		else if (source.equals(uploadButton))
		{
			this.actionUploadButton();
		}
		else if (source.equals(downloadButton))
		{
			this.actionDownloadButton();
		}
		else if (source.equals(tokenRefreshButton))
		{
			try {
				this.runCommand("refreshtoken");
			} catch (IOException e1) {
				this.cmdoutputArea.append("ERROR:cannnot refresh access token");
				e1.printStackTrace();
			}
		}
		else if (source.equals(this.settingsItem))
		{
			new SettingsDialog(this, "设置").setVisible(true);
		}
		else if (source.equals(this.aboutItem))
		{
			JOptionPane.showMessageDialog(this, 
					"Copyright 2014 Junyuan Hong\n  LICENSE under GPL v3");
		}
		else if (source.equals(this.cmdhelpItem))
		{
			helpDialog.setVisible(true);
		}
		else if (source.equals(newDirButton))
		{
			String dirName = JOptionPane.showInputDialog("Input directory name:");
			try {
				if (dirName!=null)
					this.runCommand("mkdir "+this.pwd+"/"+dirName);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		else if (source.equals(deleteButton))
		{
			this.actionDeleteButton();
		}
		else if (source.equals(syncButton))
		{
			this.actionSnycButton();
		}
//		else if (source.equals(searchButton))
//		{
//			//--------search----------
//			String filter = JOptionPane.showInputDialog("Filter:");
//			try {
//				this.runCommand("search "+filter);
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//		}
		else if (source.equals(homeButton)) {
			this.pwd = "/";
			try {
				this.ListFile(null);
			} catch (IOException e1) {
				System.out.println("Clicked Home:error occured when list file");
			}
		} else if (source.equals(searchField)) {
			/** TODO:bypy的search命令只支持搜索完整的文件名字，不支持展开
			 * 但是支持关键字搜索，使用.py可以搜到所有名字中含有.py的文件
			 * 但搜索*.py不会搜索表达式，而是搜索含有“*.py”的文件
			 */
//			JOptionPane.showMessageDialog(this, "search "+searchField.getText());
			String searchFileName = searchField.getText();
			try {
				this.searchFile(searchFileName);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this,
						"ERROR: when run search");
			}
			
		}
		
	}
	
	private void actionSnycButton() {
		int size = syncFiles.size();
		if (size>0 && remoteSyncFiles.size()==size) {
			int i = 0;
			for (i = 0; i < size; i++) {
				this.addTask(new RunCommandThread(this,
						"syncup "
						+syncFiles.elementAt(i)
						+" "
						+remoteSyncFiles.elementAt(i),
						true,
						"syncup")
				);
			}
		}
		else {
			JOptionPane.showMessageDialog(this,
					"您还未设置同步文件夹，在选项->设置内设置同步文件夹");
		}
	}



	private void actionDeleteButton() {
		int row = fileListTable.getSelectedRow();
		int count = fileListTable.getSelectedRowCount();
		int rows[] = null;
		if (count>1) {
			rows = fileListTable.getSelectedRows();
		}
		if (row==-1){
			JOptionPane.showMessageDialog(this, 
					"Please select a file");
			return;
		}
		String fileName = (String)this.tableModel.getValueAt(row, 1);
		
		if (fileName.equals("..")||fileName==null) {
			JOptionPane.showMessageDialog(this, 
					"Please select a file");
		}
		
		try {
			this.runCommand("delete "+this.pwd+"/"+fileName);
			if (count>1) {
				for (int i=1;i<count;i++) {
					fileName = (String)this.tableModel.getValueAt(rows[i], 1);
					this.runCommand("delete "+this.pwd+"/"+fileName);
				}
			}
			this.ListFile(null);
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(this, 
					"Error occured when execute bypy command");
		}
	}



	private void actionUploadButton() {
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
		fileChooser.setDialogTitle("选择上传文件");
		int stat = fileChooser.showDialog(this, "上传");
		if (stat==JFileChooser.APPROVE_OPTION) {
			File selectFile = fileChooser.getSelectedFile();
			String fileName = selectFile.getAbsolutePath();
			System.out.println("upload "+fileName+" "+this.pwd+"/"+selectFile.getName());
			this.addTask(new RunCommandThread(this, 
					"upload "+fileName+" "+this.pwd+"/"+selectFile.getName(),
					true,
					"upload "+selectFile.getName()) );
		}
	}



	private void actionDownloadButton() {
		int row = fileListTable.getSelectedRow();
		if (row==-1) {
			JOptionPane.showMessageDialog(this, "请选择一个文件或目录");
			return;
		}
		String type = (String)this.tableModel.getValueAt(row, 0);
		String fileName = (String)this.tableModel.getValueAt(row, 1);
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setDialogTitle("选择保存位置");
		int stat = fileChooser.showSaveDialog(this);
		
		if (stat==JFileChooser.APPROVE_OPTION) {
			File selectFile = fileChooser.getSelectedFile();

			String localDirName = selectFile.getAbsolutePath();
			if (type.equals("D")) {
				System.out.println("downdir "+this.pwd+"/"+fileName
						+" "+localDirName+"/"+fileName);
				this.addTask(new RunCommandThread(this, 
						"downdir "+this.pwd+"/"+fileName
						+" "+localDirName+"/"+fileName,
						true,
						"download dir:"+fileName) );
			} else if (type.equals("F")) {
				System.out.println("downfile "+this.pwd+"/"+fileName
						+" "+localDirName+"/"+fileName);
				this.addTask(new RunCommandThread(this, 
						"downfile "+this.pwd+"/"+fileName
						+" "+localDirName+"/"+fileName,
						true,
						"download file:"+fileName) );
			}
		}
	}



	protected void searchFile(String filename) throws IOException {
		//TODO: bypy search 支持的是pwd/filename还是只支持filename????
		//貌似只支持filename而且还不能是dirName
		Process ps;
		String inline;
		boolean flag = false;
		if (filename==null || filename.length()<1) {
			return;
		}
		String cmd = "search "+filename;
		this.cmdoutputArea.append("[bypy]#"+cmd);
		System.out.println("[bypy]#"+cmd);
		ps = Runtime.getRuntime().exec(bypyArgument+cmd); // throw IOException
		BufferedReader br = new BufferedReader(new InputStreamReader(
				ps.getInputStream()));
		while((inline = br.readLine())!=null)
		{
			this.cmdoutputArea.append('\n'+inline);
			if (flag)
			{
				if (!Character.isLetter(inline.charAt(0)))
				{
					break;
				}
				String col[] = inline.split(" ");
				tableModel.addRow(col);
			}
			if (inline.startsWith("Found:"))
			{
				if (tableModel.getRowCount()>0)
					tableModel.setRowCount(0);// clear
				flag = true;
			}
		}
	}

	protected void ListFile(String arg[]) throws IOException {
		Process ps;
		String inline;
		boolean flag = false;
		if (arg==null||(arg.length<=1&&arg.length>0)){
			arg = new String[2];
			arg[1] = pwd;
			arg[0] = "list";
		}
//		else if(arg.length>1){
//			this.pwd = arg[1];
//			System.out.println("++++"+pwd);
//		}
		String cmd = "";
		for (String cmdtmp:arg)
		{
			cmd = cmd+" "+cmdtmp;
		} 
		this.cmdoutputArea.append("[bypy]#"+cmd);
		System.out.println("[bypy]#"+cmd);
		ps = Runtime.getRuntime().exec(bypyArgument+cmd); // throw IOException
		BufferedReader br = new BufferedReader(new InputStreamReader(
				ps.getInputStream()));
		while((inline = br.readLine())!=null)
		{
			this.cmdoutputArea.append('\n'+inline);
			if (flag)
			{
				if (!Character.isLetter(inline.charAt(0)))
				{
					break;
				}
				String col[] = inline.split(" ");
				tableModel.addRow(col);
			}
			if (inline.startsWith("/"))
			{
				if (tableModel.getRowCount()>0)
					tableModel.setRowCount(0);// clear
				if (!this.pwd.equals("/")) {
					String firstline[] = {"D", ".."};
					tableModel.addRow(firstline);
				}
				flag = true;
			}
		}
		return;
	}
	
	protected void runCommand(String cmd) throws IOException {
		try{
			this.cmdBuf.add(cmd);
		}catch(IllegalStateException e1){
			this.cmdBuf.poll();
		}
		
		Process ps;
		if (cmd.startsWith("$")){
			cmd = cmd.substring(1);
			this.cmdoutputArea.setText("[shell]$ "+cmd);
			System.out.println("run:"+cmd);
			ps = Runtime.getRuntime().exec(cmd);
		}else {
			String arglist[] = cmd.split(" ");
			if (arglist[0].equals("list")||arglist[0].equals("ls")){
				this.ListFile(arglist);
				return;
			}
			else if(arglist[0].equals("cd")){
				if (arglist.length>1){
					if (arglist[1].equals("..")) {
						int i = pwd.lastIndexOf('/');
						if (i==-1||i==0)
							this.pwd = "/";
						else
							this.pwd = this.pwd.substring(0, i);
					} else if(arglist[1].startsWith("/")){
						this.pwd = arglist[1];
					} else {
						this.pwd = this.pwd + "/" + arglist[1];
					}
					System.out.println("pwd:"+pwd);
				} else {
					this.pwd = "/";
				}
				return ;
			}
			else if(arglist[0].equals("quota"))
			{
				this.cmdoutputArea.setText("[bypy]# "+cmd);
				System.out.println("run:"+bypyArgument+cmd);
				ps = Runtime.getRuntime().exec(bypyArgument+cmd);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						ps.getInputStream()));
				String inline;
				while((inline = br.readLine())!=null)
				{
					this.cmdoutputArea.append('\n'+inline);
					if (inline.startsWith("Quota"))
					{
						String tmp = inline.split(": ")[1];
						this.cloudSpace = Double.parseDouble(tmp.split("[BKMGT]B")[0]);
						if(tmp.charAt(tmp.indexOf('B')-1)=='T')
						{
							cloudSpace *= 1024;
						}
						inline = br.readLine();
						cmdoutputArea.append("\n"+inline);
						tmp = inline.split(": ")[1];
						this.usedSpace = Double.parseDouble(tmp.split("[BKMGT]B")[0]);
						System.out.print("space:"+cloudSpace+"GB used:"+usedSpace+"GB,");
						System.out.printf("%6.3f%%\n",100*usedSpace/cloudSpace);
					}
				}
				return;
			}
			else {
				this.cmdoutputArea.append("\n[bypy]# "+cmd);
				System.out.println("run:"+bypyArgument+cmd);
				ps = Runtime.getRuntime().exec(bypyArgument+cmd);
			}
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(
				ps.getInputStream()));
		String inline;
		while((inline = br.readLine())!=null)
		{
			this.cmdoutputArea.append('\n'+inline);
		}
	}

//	private Iterator<String> iterator;
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getSource().equals(cmdfField))
		{
			if (e.getKeyCode()==KeyEvent.VK_UP) {
				String cmd = this.cmdBuf.poll();
				if (cmd!=null)
				{
					this.cmdBuf.add(cmd);
					this.cmdfField.setText(cmd);
				}
			} else if(e.getKeyCode()==KeyEvent.VK_DOWN) {
				this.cmdfField.setText("");
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		ObjectOutputStream oos = null;
		File dataFile = new File(dataFolderString+"BYA.dat");
		try {
			if (!dataFile.exists()) {
				File dirFile = new File(dataFolderString);
				if (!dirFile.exists())
					dirFile.mkdir();
				dataFile.createNewFile();
			}
			dataFile.delete();//如果遇到不兼容的存档，不执行删除会导致IOException
			oos = new ObjectOutputStream(new FileOutputStream(
					dataFile));
			//-------init datapackage---------
			if (this.datapackage==null) {
				this.datapackage = new DataPackage();
			}
			datapackage.bypyArgument = this.bypyArgument;
			datapackage.pwd = this.pwd;
			datapackage.cloudSpace = this.cloudSpace;
			datapackage.usedSpace = this.usedSpace;
			datapackage.taskVector = this.taskVector;
			datapackage.fileTree = this.fileTree;
			datapackage.syncFiles = this.syncFiles;
			datapackage.remoteSyncFiles = this.remoteSyncFiles;
			datapackage.checkData();
			//--------write datapackage--------
			oos.writeObject(this.datapackage);
			
			//TODO:DELETE THIS LINE BEFORE COMMIT
//			dataFile.delete();
			
			oos.close();
		} catch (FileNotFoundException e) {
			System.out.println("Cannot open save file BYA.dat when windows closing");
		} catch (IOException e) {
			System.out.println("Write file failed when windows closing");
			System.out.println(this);
			e.printStackTrace();
		} finally {
			System.out.println("Exit window");
			System.exit(0);
		}
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		
	}
	
	private void initMenuBar() {
		//-----------Menu-------------
		menuBar = new AMenuBar();
		
		optionMenu = new AMenu("选项");
		settingsItem = new AMenuItem("设置");
		optionMenu.add(settingsItem);
		menuBar.add(optionMenu);
		settingsItem.addActionListener(this);

		helpMenu = new AMenu("Help");
		aboutItem = new AMenuItem("about");
		cmdhelpItem = new AMenuItem("help", KeyEvent.VK_H);
		helpMenu.add(aboutItem);
		helpMenu.add(cmdhelpItem);
		menuBar.add(helpMenu);
		aboutItem.addActionListener(this);
		cmdhelpItem.addActionListener(this);
//		helpDialog = new HelpDialog(this, "Command Help");
		
		this.setJMenuBar(menuBar);
		
	}
	
	private void initCommand() {
		cmdJLabel = new ALabel("命令");
		cmdfField = new ATextField();
		
		cmdfField.setForeground(Color.gray);
		cmdfField.setFont(new Font("serif",Font.BOLD, 12));
		cmdfField.setText("在这里输入bypy命令，或者输入$+shell命令");
		
		cmdfField.addKeyListener(this);
		cmdfField.addFocusListener(this);
		
		leftContainer.add(cmdJLabel);
		leftContainer.add(cmdfField);
		cmdfField.addActionListener(this);
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		leftMainLayout.setConstraints(cmdJLabel, gbc);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridheight = 2;
		gbc.gridwidth = 0;
		leftMainLayout.setConstraints(cmdfField, gbc);
	}
//
//    private class AntiliasText extends JTextArea {
//        public void paint(Graphics g) {
//            Graphics2D g2 = (Graphics2D) g;
//            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                                RenderingHints.VALUE_ANTIALIAS_ON);
//            super.paint(g2);
//        }
//    }
	
	private void initShellCommandOutput() {
		cmdoutputLabel = new ALabel("输出");
		cmdoutputArea = new ATextArea();
		
		cmdoutputArea.setFont(new Font("Monospaced", Font.BOLD, 12));
		cmdoutputJScrollPane = new JScrollPane(cmdoutputArea);// set scroll
		cmdoutputArea.setLineWrap(true);
		leftContainer.add(cmdoutputLabel);
		leftContainer.add(cmdoutputJScrollPane);
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		leftMainLayout.setConstraints(cmdoutputLabel, gbc);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 0;
		gbc.gridheight = 5;
		gbc.weightx = 1;
		gbc.weighty = 0.3;
		leftMainLayout.setConstraints(cmdoutputJScrollPane, gbc);
	}
	
	private void initPwdComponents() {
		this.pwdLabel = new ALabel("路径");
		this.pwdTextLabel = new ALabel(this.pwd);
		
		leftContainer.add(pwdLabel);
		leftContainer.add(pwdTextLabel);

		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		leftMainLayout.setConstraints(pwdLabel, gbc);
		gbc.gridwidth = 0;
		leftMainLayout.setConstraints(pwdTextLabel, gbc);
	}

	private void initSearchField() {
		searchLabel = new ALabel("搜索");
		searchField = new ATextField();
		
		leftContainer.add(searchLabel);
		leftContainer.add(searchField);
		
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		leftMainLayout.setConstraints(searchLabel, gbc);
		gbc.gridwidth = 0;
		leftMainLayout.setConstraints(searchField, gbc);
		
		searchField.addActionListener(this);
		
	}
	
	private void initFileTable() {
		jl_lb = new ALabel("文件");
		fileListTable = new ATable(){
			/*
			 * 重载isCellEditable方法使得表格元素无法编辑
			 **/
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
//		fileListTable.setEnabled(false);
//		fileListTable.setAutoResizeMode(ATable.AUTO_RESIZE_ALL_COLUMNS);
		
		tableModel = (DefaultTableModel)fileListTable.getModel();
		tableModel.addColumn("Type");
		tableModel.addColumn("File");
		tableModel.addColumn("Size");
		tableModel.addColumn("Date");
		tableModel.addColumn("Time");
		tableModel.addColumn("MD5");
		
		jlJScrollPane = new JScrollPane(fileListTable);
		leftContainer.add(jl_lb);
		leftContainer.add(jlJScrollPane);
		
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		leftMainLayout.setConstraints(jl_lb, gbc);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 0;
		gbc.gridheight = 5;
		gbc.weightx = 1;
		gbc.weighty = 0.8;
		leftMainLayout.setConstraints(jlJScrollPane, gbc);
		
		fileListTable.addMouseListener(this);
	}

	private void initAccessToken() {
		tokenTextField_lb = new ALabel("授权");
		tokenTextField = new ATextField();
		String tokenString =  this.checkTokenFile();
		if (tokenString!=null) {
//			tokenTextField.setText(tokenString);
			tokenTextField.setText("checked");
			tokenTextField.setEnabled(false);
		}
		// 布局设置
		leftContainer.add(tokenTextField_lb);
		gbc.fill = GridBagConstraints.HORIZONTAL;
        //该方法是为了设置如果组件所在的区域比组件本身要大时的显示情况
        //NONE：不调整组件大小。
        //HORIZONTAL：加宽组件，使它在水平方向上填满其显示区域，但是不改变高度。
        //VERTICAL：加高组件，使它在垂直方向上填满其显示区域，但是不改变宽度。
        //BOTH：使组件完全填满其显示区域。
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		leftMainLayout.setConstraints(tokenTextField_lb, gbc);
		leftContainer.add(tokenTextField);
//		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 6;
//		gbc.gridheight = 1;
		gbc.weightx = 1;
		leftMainLayout.setConstraints(tokenTextField, gbc);
		tokenRefreshButton = new AButton("Refresh Token");
		tokenRefreshButton.addActionListener(this);
		//------refresh token button--------
		leftContainer.add(tokenRefreshButton);
		gbc.gridwidth = 0;
		gbc.weightx = 0;
		leftMainLayout.setConstraints(tokenRefreshButton, gbc);
		
	}

	private String checkTokenFile() {
		try{
			// chech for PCS authorize file
			File file = new File(System.getProperties().getProperty("user.home")+"/.bypy.json");
			if (!file.exists()){
				JOptionPane.showMessageDialog(this, 
						"ERROR:have not Authorized\ncannot find ~/.bypy.json\n you may have not install bypy correctly");
				System.exit(-1);
//				System.out.println("No Token file, please check if ~/.bypy.json exists");
//				return null;
			}
			else {
				FileInputStream fis = new FileInputStream(file);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				String data = br.readLine().substring(1);
				String [] datalist = data.split("\"");
				if (!datalist[1].equals("access_token")){
					JOptionPane.showMessageDialog(this, "ERROR:there is no access token in ~/.bypy.json\n you may have not install bypy correctly");
					System.exit(-1);
				}else {
					br.close();
					return datalist[3];
				}
				br.close();
			}
		}catch (Exception e) {
			JOptionPane.showMessageDialog(this, 
					"ERROR when check for Access Token file\n you may have not install bypy correctly");
			System.exit(-1);
		}
		return null;
	}


	private void initBottomButtons() {
		//----------init GridBagConstraints------
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.ipadx = 0; // 这一行最右侧的空间
		//--------refresh--------
		refreshButton = new AButton("刷新");
		refreshButton.addActionListener(this);
		leftContainer.add(refreshButton);
		leftMainLayout.setConstraints(refreshButton, gbc);
		//---------home---------
		homeButton = new AButton("Home");
		homeButton.addActionListener(this);
		leftContainer.add(homeButton);
		leftMainLayout.setConstraints(homeButton, gbc);
		//---------upload-------
		uploadButton = new AButton("上传");
		uploadButton.addActionListener(this);
		leftContainer.add(uploadButton);
		leftMainLayout.setConstraints(uploadButton, gbc);
		//---------download-----
		downloadButton = new AButton("下载");
		downloadButton.addActionListener(this);
		leftContainer.add(downloadButton);
		leftMainLayout.setConstraints(uploadButton, gbc);
		//----------newDir--------
		newDirButton = new AButton("New Dir");
		newDirButton.addActionListener(this);
		leftContainer.add(newDirButton);
		leftMainLayout.setConstraints(newDirButton, gbc);
		//----------delete-----
		deleteButton = new AButton("删除");
		deleteButton.addActionListener(this);
		leftContainer.add(deleteButton);
		leftMainLayout.setConstraints(deleteButton, gbc);
		//----------sync-------
		syncButton = new AButton("同步");
		syncButton.addActionListener(this);
		leftContainer.add(syncButton);
		leftMainLayout.setConstraints(syncButton, gbc);
//		//--------search---------
//		searchButton = new AButton("搜索");
//		searchButton.addActionListener(this);
//		leftContainer.add(searchButton);
//		leftMainLayout.setConstraints(searchButton, gbc);
		
	}
	

	private void initSpaceBar() {
		spaceJLabel = new ALabel("空间");
		leftContainer.add(spaceJLabel);
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		leftMainLayout.setConstraints(spaceJLabel, gbc);
		
		spaceBar = new AProgressBar(0, 1000);
		leftContainer.add(spaceBar);
		gbc.gridwidth = 0;
		leftMainLayout.setConstraints(spaceBar, gbc);
	}
	
	private void initTaskTable() {
		this.taskLabel = new ALabel("Task List");
		this.taskTable = new ATable(){
			/*
			 * 重载isCellEditable方法使得表格元素无法编辑
			 **/
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		gbc.fill = GridBagConstraints.BOTH;
		rightContainer.add(taskLabel);
		gbc.gridwidth = 0;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		rightMainLayout.setConstraints(taskLabel, gbc);
		
		taskTableModel = (DefaultTableModel)taskTable.getModel();
		taskTableModel.addColumn("Hashcode");
		taskTableModel.addColumn("Name");
		taskTableModel.addColumn("startTime");

		taskScrollPane = new JScrollPane(taskTable);
		rightContainer.add(taskScrollPane);
//		gbc.gridwidth = 4;
//		gbc.gridheight = 5;
//		gbc.weightx = 1;

		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 0;
		gbc.gridheight = 2;
		gbc.weightx = 1;
		gbc.weighty = 0.01;
		rightMainLayout.setConstraints(taskScrollPane, gbc);
		
		taskTable.addMouseListener(this);
	}
	

	private void initParameters() {
		cmdBuf = new ArrayBlockingQueue<String>(32, true);
//		try {
//			this.runCommand("quota");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		//------spaceBar-----------
		// this request to runCommand("quto") firstly
		spaceBar.setValue((int)(this.usedSpace/this.cloudSpace*1000));
		spaceBar.setString(usedSpace+"GB/"+cloudSpace/1024+"TB "+
				Math.floor(this.usedSpace/this.cloudSpace*1000)/10+"%");
		spaceBar.setStringPainted(true);
	}
	/**
	 * @param rct the task to be run
	 * @return the index of task
	 */
	protected void addTask(RunCommandThread rct) {
//		/**
//		 * @param rct add and run RunCommandThread
//		 * @return
//		 */
//		private boolean addTask(RunCommandThread rct) {
//			return true;
//		}
//		this.taskTableModel.addRow(row);
		this.taskVector.add(rct);
//		int i = taskTableModel.getRowCount();//taskVector.indexOf(rct);
//		rct.setIndex(i);
		String index = String.valueOf(rct.hashCode());
//		String timeString = System;
		java.text.SimpleDateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date d=new java.util.Date();
		String time = df.format(d);
		String row[] = {index, rct.getTaskName(), time};
		this.taskTableModel.addRow(row);
//		System.out.println("Add task:"+row[0]+row[1]+row[2]);
		new Thread(rct).start();
//		return i;
	}
	/**
	 * @param rct remove RunCommandThread from the taskVector,but will not kill it
	 * @return
	 */
	private boolean removeTask(RunCommandThread rct) {
		this.taskVector.remove(rct);
//		this.taskTableModel.removeRow(rct.getIndex());
		int count = taskTableModel.getRowCount();
		for (int i = 0; i < count; i++) {
			if (Integer.valueOf((String)this.taskTableModel.getValueAt(i, 0)).equals(
					Integer.valueOf(rct.hashCode())))
			{
				this.taskTableModel.removeRow(i);
				return true;
			}
		}
		return false;
	}
	
	protected int getTaskIndex(RunCommandThread rct) {
		return this.taskVector.indexOf(rct);
	}
	
	protected void taskComplete(RunCommandThread rct) {
		if (taskVector.indexOf(rct)!=-1) {
			this.removeTask(rct);
			return;
		} else {
			this.loadingLayerUI.stop();
			this.setEnabled(true);
		}
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount()==2){
			if (e.getSource().equals(fileListTable)) {
//				int cols = fileListTable.getColumnCount();
				int row = fileListTable.getSelectedRow();
				if (this.tableModel.getValueAt(row, 0).equals("D")) {
					try {
						this.runCommand("cd "+this.tableModel.getValueAt(row, 1));
						this.ListFile(null);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}


	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}


	@Override
	public void mousePressed(MouseEvent arg0) {
		
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}


	@Override
	public void focusGained(FocusEvent e) {
		if (e.getSource().equals(cmdfField)) {
			this.cmdfField.setForeground(Color.black);
			cmdfField.setFont(new Font("serif",Font.PLAIN, 12));
			this.cmdfField.setText("");
		}
		
	}


	@Override
	public void focusLost(FocusEvent e) {
		if (e.getSource().equals(cmdfField)&&cmdfField.getText().equals("")) {
			cmdfField.setForeground(Color.gray);
			cmdfField.setFont(new Font("serif",Font.BOLD, 12));
			cmdfField.setText("在这里输入bypy命令，或者输入$+shell命令");
		}
	}
}


