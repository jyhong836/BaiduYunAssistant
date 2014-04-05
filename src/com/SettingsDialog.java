/**
 * 
 */
package com;

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
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import javax.naming.directory.DirContext;
import javax.naming.spi.DirectoryManager;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * @author jyhong
 *
 */
public class SettingsDialog extends JDialog implements ActionListener, MouseListener {
	
	private JButton addButton;
	private JButton deleteButton;
	
	private BaiduYunAssistant owner;
	private JTable fileListTable;
	private DefaultTableModel tableModel;
	private JLabel syncFileLabel;
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
		
		addButton = new JButton("Add");
		container.add(addButton);
		gbc.gridwidth = 1;
		addButton.addActionListener(this);
		mainLayout.setConstraints(addButton, gbc);
		deleteButton = new JButton("Delete");
		container.add(deleteButton);
		deleteButton.addActionListener(this);
		gbc.gridwidth = 0;
		mainLayout.setConstraints(deleteButton, gbc);
		
		initSyncFileTable();
		refreshSyncFileTable();
		this.setBounds(owner.getBounds().x,
				owner.getBounds().y, 300, 200);
//		JOptionPane.

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(addButton)) {
			FileDialog fileDialog = new FileDialog(this, "upload", FileDialog.LOAD);
			fileDialog.setVisible(true);
			if(fileDialog.getDirectory()!=null) {
				String fileName = fileDialog.getDirectory();
				String dirName1[] = fileName.split("/");
				String dirName = dirName1[dirName1.length-1];
				owner.syncFiles.add(fileName);
				owner.remoteSyncFiles.add("/"+"syncdir"+"/"+dirName);
				System.out.println("add local dir:"+fileName);
				System.out.println("add remote dir"+"/"+"syncdir"+"/"+dirName);
				this.refreshSyncFileTable();
			}
		} else if (e.getSource().equals(deleteButton)) {
//			int row = fileListTable.getSelectedRow();
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
//			String fileName = (String)this.tableModel.getValueAt(row, 1);
//			
//			if (fileName.equals("..")||fileName==null) {
//				JOptionPane.showMessageDialog(this, 
//						"Please select a file");
//			}
			
			//				this.runCommand("delete "+this.pwd+"/"+fileName);
			if (count>=1) {
				for (int i=0;i<count;i++) {
					String localFile = (String)this.tableModel.getValueAt(rows[i], 0);
					String remoteFileString = (String)this.tableModel.getValueAt(rows[i], 1);

					owner.syncFiles.remove(localFile);
					owner.remoteSyncFiles.remove(remoteFileString);
				}
			}
			this.refreshSyncFileTable();
		}
		
	}

	private void initSyncFileTable() {
		syncFileLabel = new JLabel("Sync File List");
		fileListTable = new JTable(){
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
//		fileListTable.setEnabled(false);
//		fileListTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		
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
