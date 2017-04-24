package com.hdu.ghj.mq;

import java.util.Date;

/**
 * 消息
 * @author ghj
 *
 */
public class Message {
	
	private Date created=new Date();
	
	private String date;
	
	private String topic;

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	
}
