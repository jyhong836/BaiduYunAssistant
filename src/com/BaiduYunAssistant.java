package com;

import javax.management.Query;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;



//-----------------
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
//-----------------
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;



//import java.util.ArrayBlockingQueue;
import javax.swing.JOptionPane;

public class BaiduYunAssistant extends JFrame implements ActionListener, KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1000L;
	private GridBagLayout mainLayout;
	private GridBagConstraints gbc;
	/* Menu */
//	private JMenu
	/* component */
	private JLabel jtf_lb;
	private JTextField jtf;
	private JPanel jp1;
	private JLabel cmdJLabel;
	private JTextField cmdfField;
	private JLabel cmdoutputLabel;
	private JTextArea cmdoutputArea;
	private JScrollPane cmdoutputJScrollPane;
	private DefaultTableModel listModel;
	private JLabel jl_lb;
	private JTable jl;
	private JScrollPane jlJScrollPane;
	private JLabel spaceJLabel;
	private JProgressBar spaceBar;
	
	/* Control Buttons */
	private JButton refreshButton;
	private JButton uploadButton;
	private JButton downloadButton;
	
	/* parameters */
	private String pwd; // currunt pwd
//	private StringBuffer cmdBuf = null;
	private ArrayBlockingQueue<String> cmdBuf;
	private double cloudSpace;
	private double usedSpace;

	public BaiduYunAssistant() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int framewidth = 600;
		int frameheight = 400;
		this.setBounds((int)screenSize.getWidth()/2 - framewidth/2,
				(int)screenSize.getHeight()/2 - frameheight/2,
				framewidth,
				frameheight);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        this.setFont(new Font("Serif", Font.BOLD, 20));
		this.setTitle("Baidu Yun Assistant");
		
		mainLayout = new GridBagLayout();
		gbc = new GridBagConstraints();
//
//		
		//-------------Shell Command------------
		cmdJLabel = new JLabel("command");
		cmdfField = new JTextField();
//		cmdfField.setFocusable(true);
//		cmdfField.requestFocusInWindow();
		this.cmdfField.addKeyListener(this);
		this.add(cmdJLabel);
		this.add(cmdfField);
		cmdfField.addActionListener(this);
		gbc.gridwidth = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		mainLayout.setConstraints(cmdJLabel, gbc);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridheight = 1;
		gbc.gridwidth = 0;
		mainLayout.setConstraints(cmdfField, gbc);
		//------------Shell Command Output------------
		cmdoutputLabel = new JLabel("command output");
		cmdoutputArea = new JTextArea();
		cmdoutputArea.setFont(new Font("Monospaced", Font.BOLD, 12));
		cmdoutputJScrollPane = new JScrollPane(cmdoutputArea);// set scroll
		cmdoutputArea.setLineWrap(true);
		this.add(cmdoutputLabel);
		this.add(cmdoutputJScrollPane);
		gbc.gridwidth = 1;
//		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		mainLayout.setConstraints(cmdoutputLabel, gbc);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 0;
		gbc.gridheight = 5;
		gbc.weightx = 1;
		gbc.weighty = 0.3;
		mainLayout.setConstraints(cmdoutputJScrollPane, gbc);
		//---------------List---------------
		jl_lb = new JLabel("File List");
		jl = new JTable();
//		listModel = new DefaultTableModel();
		listModel = (DefaultTableModel)jl.getModel();
		listModel.addColumn("Type");
		listModel.addColumn("File");
		listModel.addColumn("Size");
		listModel.addColumn("Date");
		listModel.addColumn("Time");
		listModel.addColumn("MD5");
//		jl.setModel(listModel);
//		jl.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		jlJScrollPane = new JScrollPane(jl);
		this.add(jl_lb);
		this.add(jlJScrollPane);
		gbc.gridwidth = 1;
//		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		mainLayout.setConstraints(jl_lb, gbc);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 0;
		gbc.gridheight = 5;
		gbc.weightx = 1;
		gbc.weighty = 0.8;
		mainLayout.setConstraints(jlJScrollPane, gbc);
		//---------PCS Token----------
		jtf_lb = new JLabel("PCS Token");
		jtf = new JTextField();
		try{
			// chech for PCS authorize file
			File file = new File(System.getProperties().getProperty("user.home")+"/.bypy.json");
			if (!file.exists()){
				JOptionPane.showMessageDialog(this, "ERROR:have not Authorized\ncannot find ~/.bypy.json");
				System.exit(-1);
			}
			else {
				FileInputStream fis = new FileInputStream(file);
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				String data = br.readLine().substring(1);
				String [] datalist = data.split("\"");
				if (!datalist[1].equals("access_token")){
					JOptionPane.showMessageDialog(this, "ERROR:have not Authorized\ncannot find ~/.bypy.json");
					System.exit(-1);
				}else {
					jtf.setText(datalist[3]);
					jtf.setEnabled(false);
				}
				br.close();
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		// 布局设置
		this.add(jtf_lb);
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
		mainLayout.setConstraints(jtf_lb, gbc);
		this.add(jtf);
//		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 0;
		gbc.gridheight = 1;
		gbc.weightx = 1;
		mainLayout.setConstraints(jtf, gbc);
		//---------------Space label---------------
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
		//-------------------------------
		
		//-------------ADD Buttons------------
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
		downloadButton = new JButton("download");
		downloadButton.addActionListener(this);
		this.add(downloadButton);
		mainLayout.setConstraints(uploadButton, gbc);
		//-----------gap------
		jp1 = new JPanel();
		this.add(jp1);
		gbc.gridwidth = 0;
//		gbc.weightx = 0;
//		gbc.weighty = 0;
		mainLayout.setConstraints(jp1, gbc);
		
		//-------init parameters--------
		cmdBuf = new ArrayBlockingQueue<String>(32, true);
		pwd = new String("/");
		try {
			this.runCommand("quota");
		} catch (IOException e) {
			e.printStackTrace();
		}
		//------spaceBar-----------
		spaceBar.setValue((int)(this.usedSpace/this.cloudSpace*1000));
		spaceBar.setString(usedSpace+"GB/"+cloudSpace/1024+"TB "+
				Math.floor(this.usedSpace/this.cloudSpace*1000)/10+"%");
		spaceBar.setStringPainted(true);
		
		this.setLayout(mainLayout);
//		this.setResizable(false);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		System.out.println("Thank you for using Baidu Yun Assistant");
		System.out.println("Coyright 2014 Junyuan Hong ( GitHub: jyhong836 )");
		System.out.println("Licensed under GPLv3");
		System.out.println("NOTE: you need to add the bypy.py (https://github.com/houtianze/bypy) to /usr/bin/bypy first");
		BaiduYunAssistant bya = new BaiduYunAssistant();
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
				this.runCommand("list /");
				this.runCommand("quota");
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
			if(fileDialog.getFile()!=null) {
				String fileName = fileDialog.getDirectory();
				fileName = fileName + fileDialog.getFile();
				System.out.println(fileName);
				try {
					this.runCommand("upload "+fileName);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		else if (e.getSource().equals(downloadButton))
		{
			
		}
		
	}

	private void ListFile(String arg[]) throws IOException {
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
				listModel.addRow(col);
			}
			if (inline.startsWith("/"))
			{
				listModel.setRowCount(0);// clear
				flag = true;
			}
		}
		return;
	}
	
	private void runCommand(String cmd) throws IOException {
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
				this.pwd = arglist[1];
				return;
			}
			else if(arglist[0].equals("quota"))
			{
				this.cmdoutputArea.append("[bypy]# "+cmd);
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
}


