package com.hdu.ghj.mq.redis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.hdu.ghj.mq.Message;
import com.hdu.ghj.mq.MessageConsumer;
import com.hdu.ghj.mq.MessageListener;
import com.hdu.ghj.mq.MessageMode;
import com.hdu.ghj.thread.StoppableThread;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.exceptions.JedisConnectionException;


public class RedisMessageConsumer implements MessageConsumer {

	private MessageMode mode;
	private List<MessageListener> listeners;
	private List<String> topics;
	private JedisPool jedisPool;
	private Jedis jedis = null;
	
	private StoppableThread thread = null;
	
	private static final Logger logger = LogManager.getLogger(RedisMessageConsumer.class);

	public synchronized void startConsume() {
		if(jedis == null) {
			jedis = jedisPool.getResource();
		}
		switch(mode) {
		case PRODUCER_CONSUMER_MODE : startPCMode(); break;
		case SUBSCRIBE_PUBLISH_MODE : startSPMode(); break;
		}
	}
	
	private void startPCMode() {
		final String[] topics = this.topics.toArray(new String[]{});
		thread = new StoppableThread() {
			
			@Override
			protected void doRun() {
				while(true) {
					if(stoped) break;
					try {
						List<String> pairs = null;
						try {
							pairs = jedis.blpop(0, topics);
						} catch(JedisConnectionException e) {
							logger.warn("redis subscriber escaped unexpected", e);
							jedis.close();
							while(true) {
								try {
									jedis = jedisPool.getResource();
									break;
								} catch(Exception e1) {
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e2) {
										if(stoped) break;
									}
								}
							}
							continue;
						}
						if(pairs == null || pairs.size() == 0) continue;
						String value = pairs.get(1);
						Message message = JSON.toJavaObject(JSON.parseObject(value), Message.class);
						for(MessageListener listener : listeners) {
							try {
								listener.onMessageReceived(Arrays.asList(new Message[]{message}));
							} catch(Exception e) {
								logger.error("", e);
								break;
							}
						}
					} catch(Exception e) {
						logger.warn("biz error", e);
					}
				}
			}
		};
		thread.start();
	}
	
	private void startSPMode() {
		final JedisPubSub suber = new JedisPubSub() {
		    public void onMessage(String channel, String messageStr) {
		    	Message message = JSON.toJavaObject(JSON.parseObject(messageStr), Message.class);
		    	for(MessageListener listener : listeners) {
					try {
						listener.onMessageReceived(Arrays.asList(new Message[]{message}));
					} catch(Exception e) {
						logger.error("", e);
						if(stoped) return;
					}
				}
		    }
		};
		thread = new StoppableThread() {
			
			@Override
			protected void doRun() {
				while(true) {
					if(stoped) break;
					try {
						logger.info("start subscribe redis server " + topics);
						jedis.subscribe(suber, topics.toArray(new String[]{}));
						logger.warn("redis subscriber escaped unexpected");
					} catch(Exception e) {
						logger.warn("redis subscriber escaped unexpected", e);
						jedis.close();
						if(stoped) break;
						while(true) {
							try {
								jedis = jedisPool.getResource();
								break;
							} catch(Exception e1) {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e2) {
									if(stoped) break;
								}
							}
						}
					}
				}
			}
		};
		
		thread.start();
	}

	public List<MessageListener> getListeners() {
		return listeners;
	}

	public void setListeners(List<MessageListener> listeners) {
		this.listeners = listeners;
	}
	
	public void addListener(MessageListener listener) {
		if(this.listeners == null) this.listeners = new ArrayList<MessageListener>();
		this.listeners.add(listener);
	}
	
	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	public List<String> getTopics() {
		return topics;
	}

	public void setTopics(List<String> topics) {
		this.topics = topics;
	}

	public void setMode(MessageMode mode) {
		this.mode = mode;
	}
	
	private boolean stoped = false;

	public synchronized void stopConsume() {
		stoped = true;
		try {
			thread.stopSelf();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}