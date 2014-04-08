/**
 * 
 */
package com;

import java.io.IOException;
import java.io.Serializable;

import javax.swing.JOptionPane;

/**
 * @author jyhong
 *
 */
public class RunCommandThread extends Thread {
//implements Runnable {
	
	private ShellCommand sc = null;
	private BaiduYunAssistant owner = null;
//	private String command;
	private String command = null;
	private boolean refresh = false;
	private String taskName = null;
	private int index = -1;
	private Thread instanceThread;
//	private int ID = 0;
	/**
	 * @param owner
	 *  offer the runCommand function
	 */
//	private RunCommandThread(BaiduYunAssistant owner, String command) {
//		this.owner = owner;
//		if (command!=null) {
//			this.command = new String[1];
//			this.command[0] = command;
//		}
//	}
	/**
	 * @param owner 
	 * @param command the command list
	 */
//	private RunCommandThread(BaiduYunAssistant owner, String command[]) {
//		this.owner = owner;
//		this.command = command;
//	}
	
//	private RunCommandThread(BaiduYunAssistant owner, String command, boolean refresh) {
//		this.refresh = refresh;
//		this.owner = owner;
//		if (command!=null) {
//			this.command = new String[1];
//			this.command[0] = command;
//		}
//	}
	public RunCommandThread(BaiduYunAssistant owner,
			String command,
			boolean refresh,
			String taskName) 
	{
//		Thread.currentThread();
		this.refresh = refresh;
		this.owner = owner;
		if (command!=null) {
			this.command = command;
		}
		this.taskName = taskName;
	}
//	
//	public void setIDandName(String taskName, int ID) {
//		this.setTaskName(taskName);
//		this.ID = ID;
//	}

	public RunCommandThread(BaiduYunAssistant owner, ShellCommand sc) {
		this.sc = sc;
		this.refresh = sc.refresh;
		this.owner = owner;
		if (sc.command!=null) {
//			this.command = new String[1];
			this.command = sc.command;
		} else {
			throw new NullPointerException();
		}
		this.taskName = sc.name;
	}

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
	
	public ShellCommand getShellCommand() {
		return sc;
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
		if (command!=null) {
//			for (String command:command) {
				try {
					this.owner.runCommand(command);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(owner, "ERROR when run command "
							+command
							+" ,it may be a bug, please report it");
					return;
				}
//				names = command.split("/");
//			}
		}
		/**
		 * NOTE:the ID may not be correct, you'd better set the taskName
		 */
//		int ID = this.owner.getTaskIndex(this);
		if (!owner.getNoTaskFinishTip()&&this.taskName!=null) {
			JOptionPane.showMessageDialog(owner, 
					this.taskName
					+" , complete");
		} 
//		else if (ID!=-1) {
//			JOptionPane.showMessageDialog(owner, 
//					"Task "+ID+"("
//					+this.hashCode()
//					+") , complete");
//		} 
//		else {
//			JOptionPane.showMessageDialog(owner, 
//					command[0]
//					+" , complete");
//		}
		try {
			if (refresh)
				this.owner.ListFile(null);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(owner, "ERROR when list files");
		}
		
		//extra works
		extTask();

//		if (this.owner)
		//自行从TaskVector上删除
//		this.owner.removeTask(this);
		this.owner.taskComplete(this);
	}
	
	/**
	 * 当需要完成额外的任务时，覆盖此方法
	 */
	public void extTask() {
		
	}

	/**
	 * @return the instanceThread
	 */
	public Thread getInstanceThread() {
		return instanceThread;
	}
	
	@Override
	public String toString() {
		return this.getTaskName();
	}

//	/**
//	 * @param instanceThread the instanceThread to set
//	 */
//	public void setInstanceThread(Thread instanceThread) {
//		this.instanceThread = instanceThread;
//	}
	
//	public void inte
	
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
