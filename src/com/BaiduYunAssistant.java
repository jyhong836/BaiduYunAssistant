package com;

//import javax.management.Query;
//import javax.swing.DefaultListModel;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;










//import java.util.ArrayBlockingQueue;
import javax.swing.JOptionPane;

/**
 * 
 * @author jyhong
 * TODO:刷新之后下载/更新云端文件目录到本地
 * TODO：sync，先检查
 * TODO:字体设置
 * TODO:如果出故障了，应当有功能按键允许用户重新新建一个JFrame
 * TODO:TaskQueue,每次创建RunCommandThread都在TaskQueue新建一个，TaskQueue最好是一个线程List
 *
 */
public class BaiduYunAssistant extends JFrame 
				implements ActionListener, 
							KeyListener,
							WindowListener,
							Serializable, MouseListener {
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
	private static final long serialVersionUID = getUID();//System.currentTimeMillis();//10001L;
	/**
	 * generate UID specific for access token
	 * @return 
	 */
	private static long getUID() {
		char[] tokenString = checkTokenFile().toCharArray();
		long UID = 0;
		for (char c:tokenString) {
			UID += (long)c;
		}
		UID += 10001010010001L;
		return UID;
	}
	
	private GridBagLayout mainLayout;
	private GridBagConstraints gbc;
	
	/* Menu */
	private JMenuBar menuBar;
	private JMenu helpMenu;
	private JMenuItem aboutItem;
	private JMenuItem cmdhelpItem;
	private final HelpDialog helpDialog = new HelpDialog(this, "Command Help");;
	
	/* component */
	private JPanel jp1;
	private JLabel cmdJLabel;
	private JTextField cmdfField;
	private JLabel cmdoutputLabel;
	private JTextArea cmdoutputArea;
	private JScrollPane cmdoutputJScrollPane;
	private DefaultTableModel tableModel;
	private JLabel jl_lb;
	private JTable fileListTable;
	private JScrollPane jlJScrollPane;
	private JLabel tokenTextField_lb;
	private JTextField tokenTextField;
	private JButton tokenRefreshButton;
	private JLabel spaceJLabel;
	private JProgressBar spaceBar;
	
	/* Control Buttons */
	private JButton refreshButton;
	private JButton uploadButton;
	private JButton downloadButton;
	private JButton newDirButton;
	private JButton deleteButton;
	private JButton syncButton;
	
	/* parameters */
	public final static String dataFolderString = new String("data/");
	private String pwd; // currunt pwd
//	private StringBuffer cmdBuf = null;
	private ArrayBlockingQueue<String> cmdBuf;
	private double cloudSpace = 0;
	private double usedSpace = 0;
	private Vector<RunCommandThread> taskVector;
	
	Image splashImage;
	private int splashWidth = 200;
	private int splashHeight = 200;
	private int framewidth = 800;
	private int frameheight = 600;

	public BaiduYunAssistant(BaiduYunAssistant bya) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		//----------splash----------
		//TODO:--Window这个类满有意思的可以研究一下----
		Window splashWindow = new Window(this);
		//下面这样做，在后面将无法获得图片的尺寸，ImageIO才真正将图片载入
//		splashImage = Toolkit.getDefaultToolkit().getImage("data/BaiduYun.png");
		try {
			splashImage = ImageIO.read(new File("data/BaiduYunSplash.png"));
			splashWidth = splashImage.getWidth(splashWindow);
			splashHeight = splashImage.getHeight(splashWindow);
			Canvas canvas = new Canvas(){
				public void paint(Graphics g) {
					g.drawImage(BaiduYunAssistant.this.splashImage, 0,0, this);
					g.drawString("Copyright 2014 JunyuanHong", 40, 220);
					g.drawString("LICENSE under GPLv3", 65, 238);
				}
			};
			splashWindow.add(canvas);
			splashWindow.setBounds((int)screenSize.getWidth()/2 - splashWidth/2,
					(int)screenSize.getHeight()/2 - splashHeight/2,
					splashWidth,
					splashHeight);
			splashWindow.setAlwaysOnTop(false);
			splashWindow.setVisible(true);
		} catch (IOException e1) {
			System.out.println("cannot find splash png, ignore it.\n Or you may check for the data/BaiduYun.png");
		}
		
//		System.out.println(this.hashCode());
//		this.setFont(new Font(Font.MONOSPACED,Font.ITALIC,12));
		if(bya!=null)
		{
			System.out.println("import data success");
			this.pwd = bya.pwd;
			this.cmdBuf = bya.cmdBuf;
			this.cloudSpace = bya.cloudSpace;
			this.usedSpace = bya.usedSpace;
		}
		
		taskVector = new Vector<RunCommandThread>();
		
		this.setBounds((int)screenSize.getWidth()/2 - framewidth/2,
				(int)screenSize.getHeight()/2 - frameheight/2,
				framewidth,
				frameheight);
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setFont(new Font("Serif", Font.BOLD, 20));
		this.setTitle("Baidu Yun Assistant");
		Image BaiduYunIcon = Toolkit.getDefaultToolkit().getImage("data/BaiduYun.png");;
		this.setIconImage(BaiduYunIcon);
		
		mainLayout = new GridBagLayout();
		gbc = new GridBagConstraints();
//		gbc.anchor = GridBagConstraints.SOUTH;// this not work
		
		//---------Menu----------
		this.initMenuBar();

		//-------------Shell Command------------
		this.initCommand();
		
		//------------Shell Command Output------------
		this.initShellCommandOutput();
		
		//---------------Table---------------
		this.initFileTable();
		
		//---------PCS Token----------
		this.initAccessToken();
		
		//---------------Space label---------------
		this.initSpaceBar();
		//-------------------------------
		
		
		//-------------ADD Buttons------------
		this.initButtons();
		
		//-----------gap------
		jp1 = new JPanel();
		this.add(jp1);
		gbc.gridwidth = 0;
		mainLayout.setConstraints(jp1, gbc);
		
		//-------init parameters--------
		this.initParameters();
		
		//----------Command init----------------
		try {
			this.runCommand("");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//-----------Frame settings--------------
		this.addWindowListener(this);
		
		this.setLayout(mainLayout);
//		this.setResizable(false);
		splashWindow.setVisible(false);
		this.setVisible(true);
	}


	public static void main(String[] args) {
		/**
		 * print the Installed Look and feels
		 */
//		for (UIManager.LookAndFeelInfo info:UIManager.getInstalledLookAndFeels()) {
//			System.out.println(info);
//		}
		System.out.println(
				UIManager.getCrossPlatformLookAndFeelClassName()+"\n"
				+UIManager.getSystemLookAndFeelClassName());
		try {
			try {
				UIManager.setLookAndFeel(
	//					com.sun.java.swing.plaf.nimbus
	//					"com.sun.java.swing.plaf.nimbus.NumbusLookAndFeel");
	//					UIManager.getSystemLookAndFeelClassName());
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
		} catch(Exception e) {
		}
		
		System.out.println("Thank you for using Baidu Yun Assistant");
		System.out.println("Coyright 2014 Junyuan Hong ( GitHub: jyhong836 )");
		System.out.println("LICENSE GPLv3");
		System.out.println("NOTE: you need to add the bypy.py (https://github.com/houtianze/bypy) to /usr/bin/bypy first");
		File byaFile = new File(BaiduYunAssistant.dataFolderString+"BYA.dat");
		ObjectInputStream ois;
		BaiduYunAssistant bya;
		try {
			ois = new ObjectInputStream(new FileInputStream(byaFile));
			bya = (BaiduYunAssistant)ois.readObject();
			bya.setVisible(true);
//			bya = new BaiduYunAssistant(bya);
			ois.close();
		} catch (FileNotFoundException e) {
			System.out.println("No saved data");
			bya = new BaiduYunAssistant(null);
		} catch (IOException e) {
			System.out.println("数据类型不兼容，可能使用了旧版本的数据，删除旧数据");
			byaFile.delete();
			System.out.print(e.toString());
			bya = new BaiduYunAssistant(null);
//			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("There is No BaiduYunAssistant Object Data in BYA.dat");
//			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(cmdfField)) {
			/* run commands */
			String cmd = cmdfField.getText();
			cmdfField.setText(null);
			try{
				runCommand(cmd);
			} catch (IOException e1) {
				this.cmdoutputArea.setText("some error occurred in execute "+cmd);
				e1.printStackTrace();
			}
		}
		else if(e.getSource().equals(refreshButton))
		{
			try {
//				this.runCommand("list /");
				this.ListFile(null);
				this.runCommand("quota");
				spaceBar.setValue((int)(this.usedSpace/this.cloudSpace*1000));
				spaceBar.setString(usedSpace+"GB/"+cloudSpace/1024+"TB "+
						Math.floor(this.usedSpace/this.cloudSpace*1000)/10+"%");
//				ListFile(null);
//				java.net.URI uri=new java.net.URI("http://www.baidu.com"); 
//				java.awt.Desktop.getDesktop().browse(uri);
			} catch (IOException e1) {
				this.cmdoutputArea.setText("some error occurred in execute 'list' command");
				e1.printStackTrace();
			}
//			catch (URISyntaxException e1) {
//				e1.printStackTrace();
//			}
		}
		else if (e.getSource().equals(uploadButton))
		{
			FileDialog fileDialog = new FileDialog(this, "upload", FileDialog.LOAD);
			fileDialog.setVisible(true);
			if(fileDialog.getDirectory()!=null) {
				String fileName = fileDialog.getDirectory();
				String remoteFileName = this.pwd;
				if (fileDialog.getFile()!=null) {
					fileName = fileName + fileDialog.getFile();
					remoteFileName += "/"+fileDialog.getFile();
				}
				System.out.println("upload "+fileName+" "+this.pwd);
//					this.runCommand("upload "+fileName+" "+this.pwd);
				this.addTask(new RunCommandThread(this, 
						"upload "+fileName+" "+remoteFileName,
						true,
						"upload "+fileName)
				);
			}
		}
		else if (e.getSource().equals(downloadButton))
		{
			try {
				int row = fileListTable.getSelectedRow();
				if (row==-1)
					return;
				String type = (String)this.tableModel.getValueAt(row, 0);
				String fileName = (String)this.tableModel.getValueAt(row, 1);
				FileDialog fileDialog = new FileDialog(this, "upload",
						FileDialog.SAVE);
				fileDialog.setVisible(true);
				if(fileDialog.getDirectory()!=null) {
					String getf = fileDialog.getFile();
					if (getf==null)
						getf = fileName;
					if (type.equals("D")) {
						this.runCommand("downdir "+this.pwd+"/"+fileName+" "
								+fileDialog.getDirectory()
								+"/"+getf);
					}else if(type.equals("F")) {
						this.runCommand("downfile "+this.pwd+"/"+fileName+" "
								+fileDialog.getDirectory()
								+"/"+getf);
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		else if (e.getSource().equals(tokenRefreshButton))
		{
			try {
				this.runCommand("refreshtoken");
			} catch (IOException e1) {
				this.cmdoutputArea.append("ERROR:cannnot refresh access token");
				e1.printStackTrace();
			}
		}
		else if (e.getSource().equals(this.aboutItem))
		{
			JOptionPane.showMessageDialog(this, 
					"Copyright 2014 Junyuan Hong\n  LICENSE under GPL v3");
		}
		else if (e.getSource().equals(this.cmdhelpItem))
		{
			helpDialog.setVisible(true);
		}
		else if (e.getSource().equals(newDirButton))
		{
			String dirName = JOptionPane.showInputDialog("Input directory name:");
			try {
				if (dirName!=null)
					this.runCommand("mkdir "+this.pwd+"/"+dirName);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		else if (e.getSource().equals(deleteButton))
		{
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
		else if(e.getSource().equals(syncButton))
		{
			//----------compare files----------
			System.out.println("Sync");
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
		ps = Runtime.getRuntime().exec("bypy "+cmd); // throw IOException
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
				System.out.println("run:"+"bypy "+cmd);
				ps = Runtime.getRuntime().exec("bypy "+cmd);
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
				this.cmdoutputArea.append("[bypy]# "+cmd);
				System.out.println("run:"+"bypy "+cmd);
				ps = Runtime.getRuntime().exec("bypy "+cmd);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
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
			oos.writeObject(this);
			
			//TODO:DELETE THIS LINE BEFORE COMMIT
//			dataFile.delete();
			
			oos.close();
		} catch (FileNotFoundException e) {
			System.out.println("Cannot open save file BYA.dat when windows closing");
		} catch (IOException e) {
			System.out.println("Write file failed when windows closing");
		} finally {
			System.out.println("Exit window");
			System.exit(0);
		}
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	private void initMenuBar() {
		//-----------Menu-------------
		menuBar = new JMenuBar();
		helpMenu = new JMenu("Help");
		aboutItem = new JMenuItem("about");
		cmdhelpItem = new JMenuItem("help", KeyEvent.VK_F1);
		helpMenu.add(aboutItem);
		helpMenu.add(cmdhelpItem);
		menuBar.add(helpMenu);
		this.setJMenuBar(menuBar);
		aboutItem.addActionListener(this);
		cmdhelpItem.addActionListener(this);
//		helpDialog = new HelpDialog(this, "Command Help");
	}
	
	private void initCommand() {
		cmdJLabel = new JLabel("command");
		cmdfField = new JTextField();
		this.cmdfField.addKeyListener(this);
		this.add(cmdJLabel);
		this.add(cmdfField);
		cmdfField.addActionListener(this);
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		mainLayout.setConstraints(cmdJLabel, gbc);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridheight = 2;
		gbc.gridwidth = 0;
		mainLayout.setConstraints(cmdfField, gbc);
	}
	
	private void initShellCommandOutput() {
		cmdoutputLabel = new JLabel("output");
		cmdoutputArea = new JTextArea();
		cmdoutputArea.setFont(new Font("Monospaced", Font.BOLD, 12));
		cmdoutputJScrollPane = new JScrollPane(cmdoutputArea);// set scroll
		cmdoutputArea.setLineWrap(true);
		this.add(cmdoutputLabel);
		this.add(cmdoutputJScrollPane);
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		mainLayout.setConstraints(cmdoutputLabel, gbc);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 0;
		gbc.gridheight = 5;
		gbc.weightx = 1;
		gbc.weighty = 0.3;
		mainLayout.setConstraints(cmdoutputJScrollPane, gbc);
	}
	
	private void initFileTable() {
		jl_lb = new JLabel("File List");
		fileListTable = new JTable(){
			/*
			 * 重载isCellEditable方法使得表格元素无法编辑
			 **/
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
//		fileListTable.setEnabled(false);
//		fileListTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
		tableModel = (DefaultTableModel)fileListTable.getModel();
		tableModel.addColumn("Type");
		tableModel.addColumn("File");
		tableModel.addColumn("Size");
		tableModel.addColumn("Date");
		tableModel.addColumn("Time");
		tableModel.addColumn("MD5");
		
		jlJScrollPane = new JScrollPane(fileListTable);
		this.add(jl_lb);
		this.add(jlJScrollPane);
		
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		mainLayout.setConstraints(jl_lb, gbc);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 0;
		gbc.gridheight = 5;
		gbc.weightx = 1;
		gbc.weighty = 0.8;
		mainLayout.setConstraints(jlJScrollPane, gbc);
		
		fileListTable.addMouseListener(this);
	}

	private void initAccessToken() {
		tokenTextField_lb = new JLabel("PCS Token");
		tokenTextField = new JTextField();
		String tokenString =  this.checkTokenFile();
		if (tokenString!=null) {
			tokenTextField.setText(tokenString);
			tokenTextField.setEnabled(false);
		}
		// 布局设置
		this.add(tokenTextField_lb);
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
		mainLayout.setConstraints(tokenTextField_lb, gbc);
		this.add(tokenTextField);
//		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		mainLayout.setConstraints(tokenTextField, gbc);
		tokenRefreshButton = new JButton("Refresh Token");
		tokenRefreshButton.addActionListener(this);
		this.add(tokenRefreshButton);
		gbc.gridwidth = 0;
		gbc.weightx = 0;
		mainLayout.setConstraints(tokenRefreshButton, gbc);
		
	}

	private static String checkTokenFile() {
		try{
			// chech for PCS authorize file
			File file = new File(System.getProperties().getProperty("user.home")+"/.bypy.json");
			if (!file.exists()){
//				JOptionPane.showMessageDialog(this, "ERROR:have not Authorized\ncannot find ~/.bypy.json");
				System.exit(-1);
			}
			else {
				FileInputStream fis = new FileInputStream(file);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				String data = br.readLine().substring(1);
				String [] datalist = data.split("\"");
				if (!datalist[1].equals("access_token")){
//					JOptionPane.showMessageDialog(this, "ERROR:have not Authorized\ncannot find ~/.bypy.json");
					System.exit(-1);
				}else {
					br.close();
					return datalist[3];
				}
				br.close();
			}
		}catch (Exception e) {
//			JOptionPane.showMessageDialog(this, 
//					"ERROR when check for Access Token file");
		}
		return null;
	}


	private void initButtons() {
		//--------refresh--------
		refreshButton = new JButton("Refresh");
		refreshButton.addActionListener(this);
		this.add(refreshButton);
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		mainLayout.setConstraints(refreshButton, gbc);
		//---------upload-------
		uploadButton = new JButton("Upload");
		uploadButton.addActionListener(this);
		this.add(uploadButton);
		mainLayout.setConstraints(uploadButton, gbc);
		//---------download-----
		downloadButton = new JButton("Download");
		downloadButton.addActionListener(this);
		this.add(downloadButton);
		mainLayout.setConstraints(uploadButton, gbc);
		//----------newDir--------
		newDirButton = new JButton("New Dir");
		newDirButton.addActionListener(this);
		this.add(newDirButton);
		mainLayout.setConstraints(newDirButton, gbc);
		//----------delete-----
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(this);
		this.add(deleteButton);
		mainLayout.setConstraints(deleteButton, gbc);
		//----------sync-------
		syncButton = new JButton("Sync");
		syncButton.addActionListener(this);
		this.add(syncButton);
		mainLayout.setConstraints(syncButton, gbc);
		
	}
	


	private void initSpaceBar() {
		spaceJLabel = new JLabel("space:");
		this.add(spaceJLabel);
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		mainLayout.setConstraints(spaceJLabel, gbc);
		spaceBar = new JProgressBar(0, 1000);
		
		this.add(spaceBar);
		gbc.gridwidth = 0;
		mainLayout.setConstraints(spaceBar, gbc);
	}
	

	private void initParameters() {
		cmdBuf = new ArrayBlockingQueue<String>(32, true);
		pwd = new String("/");
//		try {
//			this.runCommand("quota");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		//------spaceBar-----------
//		spaceBar.setValue(0);
//		spaceBar.setString("-/- 0.0%");
		spaceBar.setValue((int)(this.usedSpace/this.cloudSpace*1000));
		spaceBar.setString(usedSpace+"GB/"+cloudSpace/1024+"TB "+
				Math.floor(this.usedSpace/this.cloudSpace*1000)/10+"%");
		spaceBar.setStringPainted(true);
	}
	/**
	 * @param rct add and run RunCommandThread
	 * @return
	 */
	private boolean addTask(RunCommandThread rct) {
		this.taskVector.add(rct);
		new Thread(rct).start();//start
		return true;
	}
	/**
	 * @param rct remove RunCommandThread from the taskVector,but will not kill it
	 * @return
	 */
	protected boolean removeTask(RunCommandThread rct) {
		this.taskVector.remove(rct);
		return true;
	}
	
	protected int getTaskIndex(RunCommandThread rct) {
		return this.taskVector.indexOf(rct);
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
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}


