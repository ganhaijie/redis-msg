package com.hdu.ghj.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ObjectLock<K> {

	private Map<Object, LockObject> locks = new HashMap<Object, LockObject>();
	private HashLock<K> smallLock;

	public ObjectLock() {
		this(1000);
	}

	public ObjectLock(int innerLockSize) {
		smallLock = new HashLock<>(innerLockSize);
	}

	public void lock(K k) {
		LockObject lock = null;
		smallLock.lock(k);
		try {
			if ((lock = locks.get(k)) == null) {
				lock = new LockObject();
				locks.put(k, lock);
			}
			lock.addCount(1);
		} finally {
			smallLock.unlock(k);
		}
		lock.getLock().lock();
	}

	public void unlock(K k) {
		LockObject lock = null;
		smallLock.lock(k);

		try {
			lock = locks.get(k);
			if (lock == null)
				return;
			int count = lock.addCount(-1);
			if (count == 0) {
				locks.remove(k);
			}
		} finally {
			smallLock.unlock(k);
		}
		lock.getLock().unlock();
	}

	private static class LockObject {

		private Lock lock;
		private int count = 0;

		public LockObject() {
			lock = new ReentrantLock();
		}

		public int addCount(int count) {
			this.count += count;
			return this.count;
		}

		public Lock getLock() {
			return lock;
		}
	}

	private static int c = 0;

	public static void main(String[] args) throws Exception {
		int threadCount = 20;
		final ObjectLock<String> lock = new ObjectLock<String>();
		final Semaphore se = new Semaphore(0);
		long start = System.currentTimeMillis();
		for (int i = 0; i < threadCount; i++) {
			new Thread() {
				public void run() {
					for (int i = 0; i < 1000000; i++) {
						lock.lock("123");
						try {
							c++;
						} finally {
							lock.unlock("123");
						}
					}
					se.release(1);
				}
			}.start();
		}
		se.acquire(threadCount);
		System.out.println(c);
		System.out.println((System.currentTimeMillis() - start));
	}
}
