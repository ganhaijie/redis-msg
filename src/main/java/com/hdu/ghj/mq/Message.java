package com.hdu.ghj.mq;

import java.util.Date;

/**
 * 消息
 * @author ghj
 *
 */
public class Message {
	
	private Date created=new Date();
	
	private String data;
	
	private String topic;

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}



	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	
}
