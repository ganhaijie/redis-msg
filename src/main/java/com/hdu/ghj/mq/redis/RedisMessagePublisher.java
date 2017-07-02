package com.hdu.ghj.mq.redis;

import com.alibaba.fastjson.JSON;
import com.hdu.ghj.mq.Message;
import com.hdu.ghj.mq.MessageMode;
import com.hdu.ghj.mq.MessagePublisher;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


public class RedisMessagePublisher implements MessagePublisher {
	
    private MessageMode mode = MessageMode.PRODUCER_CONSUMER_MODE ;  //生产者消费者模式  或者  发布订阅模式
    private Jedis jedis = null;
    private JedisPool jedisPool = null;
    
	public boolean publish(Message message) {
		switch (mode) {
		case PRODUCER_CONSUMER_MODE : return publishPCMode(message);
		case SUBSCRIBE_PUBLISH_MODE : return publishSPMode(message);
		}
		throw new RuntimeException("unreached branch");
	}
	
	private boolean publishPCMode(Message message) {
		Jedis jedis = this.jedis;
		boolean fromPool = false;
		if(this.jedis == null) {
			jedis = jedisPool.getResource();
			fromPool = true;
		}
		try {
			String messageStr = JSON.toJSONString(message);
			return 1 == jedis.rpush(message.getTopic(), messageStr);
		} finally {
			if(fromPool) {
				jedis.close();
			}
		}
	}
	
	private boolean publishSPMode(Message message) {
		Jedis jedis = this.jedis;
		boolean fromPool = false;
		if(this.jedis == null) {
			jedis = jedisPool.getResource();
			fromPool = true;
		}
		try {
			String messageStr = JSON.toJSONString(message);
			return 1 == jedis.publish(message.getTopic(), messageStr);
		} finally {
			if(fromPool) {
				jedis.close();
			}
		}
	}

	public void setMode(MessageMode mode) {
		this.mode = mode;
	}

	public void setJedis(Jedis jedis) {
		this.jedis = jedis;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
}
