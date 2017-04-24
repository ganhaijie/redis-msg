package com.hdu.ghj.mq;

import java.util.List;

public interface MessageConsumer {
	void startConsume();
	void stopConsume();
	void setMode(MessageMode mode);
	void setTopics(List<String> topics);
	void setListeners(List<MessageListener> listeners);
	void addListener(MessageListener listener);
}
