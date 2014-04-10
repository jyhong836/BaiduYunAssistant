package com;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 
 * @author jyhong
 * 
 * @funciton 这是任务管理进程，从taskVector中取任务，添加到执行队列，执行。
 *
 */
public class TaskQueueThread extends Thread {
	
	protected ArrayBlockingQueue<RunCommandThread> taskQueue;
	private BaiduYunAssistant owner;
	private short maxTaskNum = 5;
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
				RunCommandThread rct;
				ShellCommand sc;
				synchronized(owner.waitTaskVector) {
					while (owner.waitTaskVector.isEmpty()) {
//						owner.stopRefreshTaskTableSchedule();
						owner.waitTaskVector.wait();
					}
					sc = owner.waitTaskVector.elementAt(0);// owner.getWaitTask();
				}
				rct = new RunCommandThread(owner, sc);
//				RunCommandThread rct = owner.getWaitTask();
//				RunCommandThread rct = owner.waitTaskVector.elementAt(0);
				this.taskQueue.put(rct);
				this.owner.removeWaitTask(rct);
				
				rct.start();
				sc.setStat(ShellCommand.STAT_RUN);
//				owner.startRefreshTaskTableSchedule();
				
//				owner.refreshTaskTable(true);
				System.out.println("start task:"+rct+sc.command);
			} catch (InterruptedException e) {
				System.out.println("TaskQueueThread is interrupted");
				return;
			} 
		}
		
//		
//		while (!stopflag) {
//			try {
//				if (!owner.waitTaskQueue.isEmpty()) {
//					this.wait();
//				}
//				else {
//					//FIXME:取任务应当设计成阻塞的，否则会消耗CPU资源
//					RunCommandThread rct = owner.waitTaskQueue.take();
//					
//					this.taskQueue.put(rct);
//					rct.start();
////					owner.waitTaskVector.remove(0);
//				}
//			} catch (InterruptedException e) {
//				/* 阻塞过程中发生中断:
//				 * 1.rct还未取出
//				 * 2.rct已经取出，正在put，这时发生终端，导致rct可能丢失
//				 */
//				System.out.println("taskQueueThread is interrupted:1");
//				if (stopflag) {
//					this.killRunningTask();
//					return;
//				}else {
//					
//				}
//				/**else 程序继续，可能是在其他线程中发生需要删除一个RCT时，临时请求中断，避免
//				 *添加已经被删除的RCT
//				 */
//			}
//		}
		this.killRunningTask();
		System.out.println("taskQueueThread is interrupted:0");
	}
	
	/**
	 * kill all running task
	 */
	protected void killRunningTask() {
//		this.setStopflag(true);
//		this.wait();
		this.interrupt(); // 先停止当前的行为，避免继续加入新的任务
		
		RunCommandThread rct;
		while((rct = this.taskQueue.poll())!=null) {
			this.owner.addWaitTask(0, rct);
			rct.stop();//TODO:以后会考虑更好的处理方法
//			owner.waitTaskVector.add(0, rct);
		}
//		this.notify();//TODO:TEST
	}
	/**
	 * stop and remove the running task
	 * @param rct -RunCommandThread to be kill
	 * @return true if success
	 */
	protected boolean killRunningTask(RunCommandThread rct) {
//		this.setStopflag(false);
//		this.wait();
		this.interrupt(); // 先中断，避免继续加入新的任务
		rct.stop();
		boolean ret = this.taskQueue.remove(rct);
		if (true)
			this.owner.addWaitTask(0, rct);
//		this.notify();//TODO:TEST
		return ret;
	}
	
	/**
	 * remove RunCommandThread but not kill it
	 * @param rct -RunCommandThread to be remove
	 * @return true if success
	 */
	public boolean removeFinishedTask(RunCommandThread rct) {
		System.out.println("removeFinishedTask"+rct);
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
