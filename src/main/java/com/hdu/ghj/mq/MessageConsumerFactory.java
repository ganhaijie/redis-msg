package com.hdu.ghj.mq;

import java.util.List;

public interface MessageConsumerFactory {

	public MessageConsumer create(List<String> topics, MessageMode mode, List<MessageListener> listeners);
}
