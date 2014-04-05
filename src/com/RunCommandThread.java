/**
 * 
 */
package com;

import java.awt.color.CMMException;
import java.io.IOException;

import javax.swing.JOptionPane;

/**
 * @author jyhong
 *
 */
public class RunCommandThread implements Runnable {
	
	private BaiduYunAssistant owner = null;
//	private String command;
	private String cmdList[] = null;
	private boolean refresh = false;
	private String taskName = null;
	private int index = -1;
//	private int ID = 0;
	/**
	 * @param owner
	 *  offer the runCommand function
	 */
	private RunCommandThread(BaiduYunAssistant owner, String command) {
		this.owner = owner;
		if (command!=null) {
			this.cmdList = new String[1];
			this.cmdList[0] = command;
		}
	}
	/**
	 * @param owner 
	 * @param command the command list
	 */
	private RunCommandThread(BaiduYunAssistant owner, String command[]) {
		this.owner = owner;
		this.cmdList = command;
	}
	
	private RunCommandThread(BaiduYunAssistant owner, String command, boolean refresh) {
		this.refresh = refresh;
		this.owner = owner;
		if (command!=null) {
			this.cmdList = new String[1];
			this.cmdList[0] = command;
		}
	}
	public RunCommandThread(BaiduYunAssistant owner,
			String command,
			boolean refresh,
			String taskName) {
		this.refresh = refresh;
		this.owner = owner;
		if (command!=null) {
			this.cmdList = new String[1];
			this.cmdList[0] = command;
		}
		this.taskName = taskName;
	}
//	
//	public void setIDandName(String taskName, int ID) {
//		this.setTaskName(taskName);
//		this.ID = ID;
//	}

	/**
	 * @return the taskName
	 */
	public String getTaskName() {
		return taskName;
	}

	/**
	 * @param taskName the taskName to set
	 */
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
//
//	/**
//	 * @return the iD
//	 */
//	public int getID() {
//		return ID;
//	}

//	/**
//	 * @param iD the iD to set
//	 */
//	public void setID(int iD) {
//		ID = iD;
//	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
//		String names[] = null;
		if (cmdList!=null) {
			for (String command:cmdList) {
				try {
					this.owner.runCommand(command);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(owner, "ERROR when run command "
							+command
							+" ,it may be a bug, please report it");
					return;
				}
//				names = command.split("/");
			}
		}
		/**
		 * NOTE:the ID may not be correct, you'd better set the taskName
		 */
		int ID = this.owner.getTaskIndex(this);
		if (this.taskName!=null) {
			JOptionPane.showMessageDialog(owner, 
					this.taskName
					+" , complete");
		} else if (ID!=-1) {
			JOptionPane.showMessageDialog(owner, 
					"Task "+ID+"("
					+this.hashCode()
					+") , complete");
		} else {
			JOptionPane.showMessageDialog(owner, 
					cmdList[0]
					+" , complete");
		}
		try {
			if (refresh)
				this.owner.ListFile(null);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(owner, "ERROR when list files");
		}

		//自行从TaskVector上删除
		this.owner.removeTask(this);
	}
	/**
	 * @return the index
	 */
//	public int getID() {
//		return ID;
//	}
//	/**
//	 * @param index the index to set
//	 */
//	public void setID(int id) {
//		this.id = id;
//	}

}
