package com.commerce.web.request;

import javax.validation.constraints.NotBlank;

public class CustomerPostRequest {
	
	@NotBlank
	private String email;
	
	@NotBlank
	private String customerId;

	@NotBlank
	private String name;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
