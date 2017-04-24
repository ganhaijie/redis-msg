package com.hdu.ghj.mq;

import java.util.List;

public interface MessageListener {
	ConsumeResult onMessageReceived(List<Message> messages);
}
