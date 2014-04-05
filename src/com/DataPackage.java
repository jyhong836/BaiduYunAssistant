/**
 * 
 */
package com;

import java.io.Serializable;
import java.util.Vector;
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
	protected Vector<RunCommandThread> taskVector;

	/**
	 * 
	 */
	public DataPackage() {
	}

}
