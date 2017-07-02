package com.hdu.ghj.mq;

public interface MessagePublisher {
	 boolean publish(Message messages);
	 void setMode(MessageMode mode);
}
