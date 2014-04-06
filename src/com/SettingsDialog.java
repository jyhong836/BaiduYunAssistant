/**
 * 
 */
package com;

import com.Antilias.*;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import javax.naming.directory.DirContext;
import javax.naming.spi.DirectoryManager;
//import javax.swing.AButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
//import javax.swing.ALabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
//import javax.swing.ATable;
import javax.swing.table.DefaultTableModel;

/**
 * @author jyhong
 *
 */
public class SettingsDialog extends JDialog implements ActionListener, MouseListener {

	private BaiduYunAssistant owner;
	
	private AButton addButton;
	private AButton deleteButton;
	private AButton deleteAllButton;
	
	private ATable fileListTable;
	private DefaultTableModel tableModel;
	private ALabel syncFileLabel;
	
	private ALabel argumentLabel;
	private ATextField argumentField;
	
	private AButton applyButton;
	
	private Container container;
	private JScrollPane tableJScrollPane;
	private GridBagLayout mainLayout;
	private GridBagConstraints gbc;

	/**
	 * @param owner
	 * @param title
	 * 在打开的选择目录中只能先进入要同步的目录再点确定，
	 * 即使选择了文件也会被忽略，只需要文件夹名
	 */
	public SettingsDialog(BaiduYunAssistant owner, String title) {
		super(owner, title);
		this.owner = owner;
		container = this.getContentPane();
		mainLayout = new GridBagLayout();
		container.setLayout(mainLayout);
		
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 0;
		
		//-------init buttons------
		this.initButtons();
		
		//-------init sync file table----
		initSyncFileTable();
		refreshSyncFileTable();
		
		//---------init argument field----
		initArgumentField();
		
		//--------init applyButton-----
		initApplyButton();
		
		this.setBounds(owner.getBounds().x,
				owner.getBounds().y, 300, 400);
//		JOptionPane.

	}

	private void initApplyButton() {
		JPanel applySpaceJPanel1 = new JPanel();
		JPanel applySpaceJPanel2 = new JPanel();
		applyButton = new AButton("应用");
		gbc.fill = GridBagConstraints.NONE;
		container.add(applySpaceJPanel1);
		container.add(applySpaceJPanel2);
		container.add(applyButton);
		
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.gridheight = 1;
		mainLayout.setConstraints(applySpaceJPanel1, gbc);
		mainLayout.setConstraints(applySpaceJPanel2, gbc);
		mainLayout.setConstraints(applyButton, gbc);
		
		applyButton.addActionListener(this);
		
	}

	private void initArgumentField() {
		//----------init GridBagConstraints------
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.ipadx = 0; // 这一行最右侧的空间
		
		this.argumentLabel = new ALabel("参数");
		this.argumentField = new ATextField(this.owner.bypyArgument);
		container.add(argumentLabel);
		gbc.gridwidth = 1;
		mainLayout.setConstraints(argumentLabel, gbc);
		container.add(argumentField);
		gbc.gridwidth = 0;
		mainLayout.setConstraints(argumentField, gbc);
//		addButton.addActionListener(this);
		
	}

	/**
	 * @Method initButtons 
	 * 		Add, Delete, DeleteAll
	 */
	private void initButtons() {
		//----------init GridBagConstraints------
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.ipadx = 10; // 这一行最右侧的空间
		//------------Add-------------
		addButton = new AButton("添加");
		container.add(addButton);
		gbc.gridwidth = 1;
		addButton.addActionListener(this);
		mainLayout.setConstraints(addButton, gbc);
		//------------Delete-------------
		deleteButton = new AButton("删除");
		container.add(deleteButton);
		deleteButton.addActionListener(this);
		mainLayout.setConstraints(deleteButton, gbc);
		//-------------Delete All---------
		deleteAllButton = new AButton("删除所有");
		container.add(deleteAllButton);
		deleteAllButton.addActionListener(this);
		gbc.gridwidth = 0;
		mainLayout.setConstraints(deleteAllButton, gbc);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source.equals(addButton)) {			
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
//			fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
			fileChooser.setDialogTitle("choose sync dir or file");
			int stat = fileChooser.showOpenDialog(this);
			
			if (stat==JFileChooser.APPROVE_OPTION) {
				File selectFile = fileChooser.getSelectedFile();
				owner.syncFiles.add(selectFile.getAbsolutePath());
				owner.remoteSyncFiles.add("/sync/"+selectFile.getName());
				this.refreshSyncFileTable();
			}
		} else if (source.equals(deleteButton)) {
			int count = fileListTable.getSelectedRowCount();
			int rows[] = null;
			if (count>=1) {
				rows = fileListTable.getSelectedRows();
			}
			if (count<1){
				JOptionPane.showMessageDialog(this, 
						"Please select a directory");
				return;
			}
			if (count>=1) {
				for (int i=0;i<count;i++) {
					String localFile = (String)this.tableModel.getValueAt(rows[i], 0);
					String remoteFileString = (String)this.tableModel.getValueAt(rows[i], 1);

					owner.syncFiles.remove(localFile);
					owner.remoteSyncFiles.remove(remoteFileString);
				}
			}
			this.refreshSyncFileTable();
		} else if(source.equals(deleteAllButton)) {
			owner.syncFiles.removeAllElements();
			owner.remoteSyncFiles.removeAllElements();
			this.refreshSyncFileTable();
		} else if(source.equals(applyButton)) {
			owner.bypyArgument = this.argumentField.getText();
		}
		
	}

	private void initSyncFileTable() {
		syncFileLabel = new ALabel("同步列表");
		fileListTable = new ATable(){
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
//		fileListTable.setEnabled(false);
//		fileListTable.setAutoResizeMode(ATable.AUTO_RESIZE_ALL_COLUMNS);
		
		tableModel = (DefaultTableModel)fileListTable.getModel();
		tableModel.addColumn("Local Dir");
		tableModel.addColumn("Remote Dir");
		
		tableJScrollPane = new JScrollPane(fileListTable);
		container.add(syncFileLabel);
		container.add(tableJScrollPane);
		
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0;
		gbc.weighty = 0;
		mainLayout.setConstraints(syncFileLabel, gbc);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 0;
		gbc.gridheight = 5;
		gbc.weightx = 1;
		gbc.weighty = 0.8;
		mainLayout.setConstraints(tableJScrollPane, gbc);
		
		fileListTable.addMouseListener(this);
	}
	
	private void refreshSyncFileTable() {
		this.tableModel.setRowCount(0);
		if (owner.syncFiles!=null)
			for (int i = 0; i < owner.syncFiles.size(); i++) {
				System.out.println(i+":"+owner.syncFiles.elementAt(i)+"\n---->"+owner.remoteSyncFiles.elementAt(i));
				String localName = owner.syncFiles.elementAt(i);
				String remoteName = owner.remoteSyncFiles.elementAt(i);
				String[] fileStrings = {localName, remoteName};
				tableModel.addRow(fileStrings);
				
			}
		else {
			owner.syncFiles = new Vector<String>();
			owner.remoteSyncFiles = new Vector<String>();
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
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
