package com.hdu.ghj.mq;

import java.util.List;

public class ConsumeResult {

	private boolean success;
	private List<Message> errorMessages;

	public boolean isSuccess() {
		return success;
	}

	public List<Message> getErrorMessages() {
		return errorMessages;
	}

	public static ConsumeResult success() {
		ConsumeResult cr = new ConsumeResult();
		cr.success = true;
		return cr;
	}
	
	public static ConsumeResult error(List<Message> errorMessages) {
		ConsumeResult cr = new ConsumeResult();
		cr.success = false;
		cr.errorMessages = errorMessages;
		return cr;
	}
	
}
