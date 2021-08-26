package com.commerce.web.request;

import javax.validation.constraints.NotBlank;

public class CustomerPutRequest {

	@NotBlank
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
