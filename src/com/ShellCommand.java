package com;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

public class ShellCommand implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7223043872129510365L;
	
	protected String command;
	protected String name;
	protected boolean refresh;
	private String startDate;//**
	private String endDate;
	protected int hashCode = this.hashCode();
	protected double percent;
	protected File localFile = null;
//	protected String fileName;
	protected int fileSize = 0;
	protected long completeSize = 0;
	protected double speed;
	
	protected long startTime = 0;//**:init when start run
	protected long speedUpdateTime = 0;//**
	
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
		if (stat==STAT_RUN) {
			this.startTime = System.currentTimeMillis();
			speedUpdateTime = startTime;
//			startDate = new Date().getT;
			SimpleDateFormat sdf = new SimpleDateFormat("",Locale.SIMPLIFIED_CHINESE);
			sdf.applyPattern("yyyy年MM月dd日_HH-mm-ss");
			this.startDate = sdf.format(new Date());
		} else if (stat==STAT_COMPLETE) {
			SimpleDateFormat sdf = new SimpleDateFormat("",Locale.SIMPLIFIED_CHINESE);
			sdf.applyPattern("yyyy年MM月dd日_HH-mm-ss");
			this.endDate = sdf.format(new Date());

			this.speed = this.fileSize*1.0/(double)(System.currentTimeMillis()-startTime);
			this.speed = this.speed/1000/1000;//换算成kb/s
		}
	}
	
	public int getStat() {
		return this.stat;
	}
	
	public String getStatString() {
		switch (stat) {
		case STAT_RUN:{
			if (localFile==null || fileSize==0)
				return "Running";
			else {
				long newcompleteSize = localFile.length();
//				System.out.println(newcompleteSize+"/"+fileSize);
				long curtime = System.currentTimeMillis();
				this.speed = (newcompleteSize - completeSize)*1.0/(double)(curtime-speedUpdateTime);
				this.speed = (this.speed*1000.0/1000.0);//换算成kb/s
				speedUpdateTime = curtime;
				completeSize = newcompleteSize;
				return (String.valueOf((int)(1000*completeSize/(double)this.fileSize)/10.0)
						+"%,"+String.valueOf((int)this.speed)+"kb/s");
			}
		}
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

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

//	/**
//	 * @param endDate the endDate to set
//	 */
//	public void setEndDate(String endDate) {
//		this.endDate = endDate;
//	}
	
//	public void getSpeed() {
//		this.return 
//	}
	
//	public void setFileName(String fileName) {
//		this.fileName = fileName;
//	}
	
	public void setFileMsg(String localfile, int filesize) {
//		this.fileName = localfile;
		this.fileSize = filesize;
		this.localFile = new File(localfile);
		this.completeSize = this.localFile.length();
	}

}
