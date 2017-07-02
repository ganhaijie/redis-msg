package com.hdu.ghj.mq;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.hdu.ghj.thread.StoppableThread;

/**
 * 异步的消息队列发布
 */
public class AsyncMessagePublisher {

	private MessagePublisher publisher;
	
	private int sendThreadCount;
	
	private BlockingQueue<Message> messageBuffer = null;
	
	private List<StoppableThread> threads = new ArrayList<StoppableThread>();
	
	public AsyncMessagePublisher(MessagePublisher publisher) {
		this(publisher, 1000, 1);
	}
	
	public AsyncMessagePublisher(MessagePublisher publisher, int bufferSize, int sendThreadCount) {
		this.publisher = publisher;
		this.sendThreadCount = sendThreadCount;
		messageBuffer = new LinkedBlockingQueue<Message>(bufferSize);
	}
	
	
	public void publish(Message message) {
		try {
			messageBuffer.put(message);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public synchronized void stop() throws InterruptedException {
		for(StoppableThread thread : threads) {
			thread.stopSelf();
		}
	}
	
	public synchronized void start() {
		for(int i = 0; i < sendThreadCount; i++) {
			StoppableThread thread = createThread();
			thread.start();
			threads.add(thread);
		}
	}
	
	private StoppableThread createThread() {
		return new StoppableThread() {
			protected void doRun() throws InterruptedException {
				try {
					Message message = messageBuffer.take();
					publisher.publish(message);
				} catch (Exception e) {
					if(this.isStopped()) {
						throw new RuntimeException(e);
					}
					Thread.sleep(1000);
				}
			}
		};
	}
}