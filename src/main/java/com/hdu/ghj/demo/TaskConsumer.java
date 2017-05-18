package com.hdu.ghj.demo;

import java.util.Random;

import redis.clients.jedis.Jedis;

public class TaskConsumer implements Runnable{

	Jedis jedis=new Jedis("127.0.0.1",6379);
	
	@Override
	public void run() {
	      Random random = new Random();  
	        while(true){  
	            String taskid = jedis.rpoplpush("task-queue", "tmp-queue");  
	              
	            try {  
	                Thread.sleep(1000);  
	            } catch (InterruptedException e) {  
	                e.printStackTrace();  
	            }  
	              
	         
	            if(random.nextInt(13) % 7 == 0){// 模拟失败的情况,概率为2/13  
	                jedis.rpoplpush("tmp-queue", "task-queue");  
	                System.out.println(taskid + "处理失败，被弹回任务队列");  
	              
	            } else {
	                jedis.rpop("tmp-queue");  
	                System.out.println(taskid+"处理成功，被清除");  
	                  
	            }     
	        }  
		
	}

}
