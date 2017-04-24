package com.hdu.ghj.mq.redis;

import java.util.List;

import com.hdu.ghj.mq.MessageConsumer;
import com.hdu.ghj.mq.MessageConsumerFactory;
import com.hdu.ghj.mq.MessageListener;
import com.hdu.ghj.mq.MessageMode;

import redis.clients.jedis.JedisPool;

public class RedisMessageConsumerFactory implements MessageConsumerFactory{

	private JedisPool jedisPool;
	
	
	public MessageConsumer craete(List<String> topics, MessageMode mode, List<MessageListener> listeners) {
		RedisMessageConsumer  consumer = new RedisMessageConsumer();
		consumer.setListeners(listeners);
		consumer.setTopics(topics);
		consumer.setMode(mode);
		consumer.setJedisPool(jedisPool);
		return consumer;
	}


	public JedisPool getJedisPool() {
		return jedisPool;
	}


	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	
}
