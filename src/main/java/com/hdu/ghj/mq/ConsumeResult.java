package com.hdu.ghj.mq;

import java.util.List;

public class ConsumeResult {
	private boolean success;
	private List<Message> errorMessages;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<Message> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(List<Message> errorMessages) {
		this.errorMessages = errorMessages;
	}

}
