/**
 * 
 */
package com;

import java.io.Serializable;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.JTree;

/**
 * @author jyhong
 *
 */
public class DataPackage implements Serializable{
	
	/**
	 * TODO:为了安全起见，最好在serial里面加入token的信息
	 */
	private static final long serialVersionUID = 4222047989282063004L;

	protected JTree fileTree;
	
	/* parameters */
	protected String pwd; // currunt pwd
	protected double cloudSpace = 0;
	protected double usedSpace = 0;
	protected Vector<ShellCommand> waitTaskVector;
	protected Vector<String> syncFiles;
	protected Vector<String> remoteSyncFiles;
	protected String bypyArgument = "bypy ";// argument shoud be add after this

	protected Vector<ShellCommand> taskVector;

	/**
	 * 
	 */
	public DataPackage() {
	}
	
	public void checkData() {
//		System.out.println("DATA CHECK:");
//		for (int i = 0; i < this.syncFiles.size(); i++) {
//			System.out.println(i+":"+this.syncFiles.elementAt(i)+"\n---->"+this.remoteSyncFiles.elementAt(i));
//			
//		}
	}

}
