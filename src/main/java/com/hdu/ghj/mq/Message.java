package com.hdu.ghj.mq;

import java.util.Date;


public class Message {

	private Date created = new Date();   //创建时间
	private String data;  //内容
	private String topic;  //redis里头就是key
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
