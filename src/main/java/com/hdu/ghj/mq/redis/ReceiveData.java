package com.hdu.ghj.mq.redis;

/**
 * Message中的data 传得是一个json
 * 这个类就是data  只是工作中需要权限的控制 就写了这样一个例子
 * 可以随意设变量 在json解析的时候解析ok就可以
 * @author ghj
 *
 */
public class ReceiveData {
	private String type;
	private Long uid;
	private Long roleId;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
}
