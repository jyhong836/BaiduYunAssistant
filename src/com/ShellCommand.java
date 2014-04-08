package com;

import java.io.Serializable;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

public class ShellCommand implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7223043872129510365L;
	
	protected String command;
	protected String name;
	protected boolean refresh;
	
	//-------命令的运行状态---------
	public static final int STAT_RUN = 1;
	public static final int STAT_WAIT = 0;
	public static final int STAT_COMPLETE = 2;
	public static final int STAT_STOP = -1;
	public static final int STAT_CANCEL = -2;
	protected int stat = STAT_WAIT;

	public ShellCommand(String command, boolean refresh, String name) {
		this.command = command;
		this.name = name;
		this.refresh = refresh;
	}
	
	public String getTaskName() {
		return name;
	}
	
	@Override
	public String toString () {
		return name;
	}
	
	public void setStat(int stat) {
		this.stat = stat;
	}
	
	public int getStat() {
		return this.stat;
	}
	
	public String getStatString() {
		switch (stat) {
		case STAT_RUN:
			return "Running";
		case STAT_STOP:
			return "Stopped";
		case STAT_CANCEL:
			return "Canceled";
		case STAT_WAIT:
			return "Waiting";
		case STAT_COMPLETE:
			return "Complete";
		default:
			return "Unknown";
		}
	}
	
//	public void setStatString(String stat) {
//		if (stat.equals("Running"))
//	}

}
