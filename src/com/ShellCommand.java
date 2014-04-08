package com;

import java.io.Serializable;

public class ShellCommand implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7223043872129510365L;
	
	protected String command;
	protected String name;
	protected boolean refresh;

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

}
