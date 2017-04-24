package com.hdu.ghj.mq.redis;

import java.util.Arrays;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hdu.ghj.mq.ConsumeResult;
import com.hdu.ghj.mq.Message;
import com.hdu.ghj.mq.MessageListener;
import com.hdu.ghj.mq.MessageMode;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class TestRedisMq {
	private static String host = "localhost";
	private static int port = 6379;

	
	private static String str="message";
	
	public static void main(String[] args) throws Exception {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(2);
		config.setTestOnBorrow(true);
		JedisPool jp = new JedisPool(config, host, port);
		RedisMessageConsumer consumer = new RedisMessageConsumer();
		consumer.addListener(new MessageListener() {
			public ConsumeResult onMessageReceived(List<Message> messages) {
				JSONArray object=(JSONArray) JSON.parse(messages.get(0).getData());
				JSONObject o=(JSONObject) object.get(0);
				System.out.println(o.get("type"));
				return ConsumeResult.success();
			}
		});
		consumer.setMode(MessageMode.PRODUCER_CONSUMER_MODE);
		consumer.setTopics(Arrays.asList(new String[]{"message"}));
//		consumer.setJedis(jedis1);
		consumer.setJedisPool(jp);
		consumer.startConsume();
		RedisMessagePublisher publiser = new RedisMessagePublisher();
		publiser.setJedisPool(jp);
		publiser.setMode(MessageMode.PRODUCER_CONSUMER_MODE);
		Integer i = 0;
		while(true) {
			Message message = new Message();
			message.setTopic(str);
			String json="[{\"type\":18,\"uid\":123,\"roleid\":27}]";
			message.setData(json);
			try {
				publiser.publish(message);
			} catch(Exception e) {
				Thread.sleep(1000);
			}
		}
	}
}
