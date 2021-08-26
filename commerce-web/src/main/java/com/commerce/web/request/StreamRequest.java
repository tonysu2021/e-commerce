package com.commerce.web.request;

import javax.validation.constraints.NotBlank;

public class StreamRequest {
	@NotBlank
	private String payload;

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

}
