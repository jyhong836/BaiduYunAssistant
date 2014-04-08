package com;

import java.util.concurrent.ArrayBlockingQueue;

public class TaskQueueThread extends Thread {
	
	private ArrayBlockingQueue<RunCommandThread> taskQueue;
	private BaiduYunAssistant owner;
	private short maxTaskNum = 10;
	private boolean stopflag = false;

	/**
	 * 
	 * @param owner
	 */
	public TaskQueueThread(BaiduYunAssistant owner) {
		this.owner = owner;
		this.taskQueue = new ArrayBlockingQueue<RunCommandThread>(maxTaskNum);
	}
	
	@Override
	public void run() {
		while (!stopflag) {
			try {
				if (!owner.taskVector.isEmpty()) {
					RunCommandThread rct = owner.taskVector.firstElement();
					this.taskQueue.put(rct);
					owner.taskVector.remove(0);
				}
			} catch (InterruptedException e) {
				// 在put的阻塞过程中发生中断，
				System.out.println("taskQueueThread is interrupted:1");
				if (stopflag) {
					this.killRunningTask();
					return;
				}
				/**else 程序继续，可能是在其他线程中发生需要删除一个RCT时，临时请求中断，避免
				 *添加已经被删除的RCT
				 */
			}
		}
		this.killRunningTask();
		System.out.println("taskQueueThread is interrupted:0");
	}
	
	/**
	 * kill all running task
	 */
	private void killRunningTask() {
		RunCommandThread rct;
		while((rct = this.taskQueue.poll())!=null) {
			rct.stop();//TODO:以后会考虑更好的处理方法
			owner.taskVector.add(0, rct);
		}
	}
	/**
	 * stop and remove the running task
	 * @param rct -RunCommandThread to be kill
	 * @return true if success
	 */
	public boolean killRunningTask(RunCommandThread rct) {
		rct.stop();
		boolean ret = this.taskQueue.remove(rct);
		if (true)
			owner.taskVector.add(0, rct);
		return ret;
	}
	
	/**
	 * remove RunCommandThread but not kill it
	 * @param rct -RunCommandThread to be remove
	 * @return true if success
	 */
	public boolean removeFinishedTask(RunCommandThread rct) {
		return this.taskQueue.remove(rct);
	}

	/**
	 * @return the stopflag
	 */
	public boolean getStopflag() {
		return stopflag;
	}

	/**
	 * @param stopflag the stopflag to set
	 */
	public void setStopflag(boolean stopflag) {
		this.stopflag = stopflag;
	}

}
