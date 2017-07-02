package com.hdu.ghj.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;




/**
 * 可停止的线程 调用停止 线程会立马停止，而不会等待一次循环结束再停止
 * 
 * 调用停止接口时，线程会不停尝试中断
 */
public abstract class StoppableThread extends Thread {

	private Lock lock = new ReentrantLock();
	
	private boolean stopped = false;
	
	public void run() {
		while(true) {
			lock.lock();
			try {
				if(!stopped) {
					try {
						doRun();
					} catch (InterruptedException e) {
						if(stopped) break;
					}
				} else {
					break;
				}
			} finally {
				lock.unlock();
			}
		}
		System.out.println("线程退出");
	}
	
	protected abstract void doRun() throws InterruptedException;
	
	public void stopSelf() throws InterruptedException {
		if(Thread.currentThread() == this) {  //自己不能停止自己
			throw new RuntimeException("current thread cannot stop itseft");
		}
		stopped = true;
		while(true) {
			this.interrupt();
			if(lock.tryLock(300, TimeUnit.MILLISECONDS)) {
				break;
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		StoppableThread thread = new StoppableThread() {
			@Override
			protected void doRun() {
				System.out.println("执行....");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		};
		thread.start();
		Thread.sleep(1000);
		thread.stopSelf();
	}
	
	public boolean isStopped() {
		return this.stopped;
	}
	
}