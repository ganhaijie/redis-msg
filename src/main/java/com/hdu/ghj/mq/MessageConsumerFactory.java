package com.hdu.ghj.mq;

import java.util.List;


public interface MessageConsumerFactory {
	public MessageConsumer craete(List<String> topics,MessageMode mode,List<MessageListener> listeners);
}
