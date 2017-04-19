package com.hdu.ghj.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class HashLock<K> {
	
	private Lock[] locks =null;
	
	public HashLock(int size){
		locks =new Lock[size];
		for(int i=0;i<size;i++){
			locks[i] = new ReentrantLock();
		}
	}
	
	private int hash(K k){
		return Math.abs(k.hashCode())%locks.length;
	}
	
	public void lock(K k){
		locks[hash(k)].lock();
	}
	
	public void unlock(K k){
		locks[hash(k)].unlock();
	}
}
