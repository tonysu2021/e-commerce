package com.commerce.web.exception;

import java.util.HashMap;
import java.util.Map;

public enum ExceptionCode {

	/** 412 series */
	CUSTOMER_EXISTED(412001, "Customer is existed"), 
	CUSTOMER_NOT_EXIST(412002, "Customer not exist"),
	/** Other */
	NONE(0, "None");

	private static final Map<Integer, ExceptionCode> BY_LABEL = new HashMap<>();

	static {
		for (ExceptionCode element : values()) {
			BY_LABEL.put(element.getCode(), element);
		}
	}

	ExceptionCode(final int code, final String message) {
		this.code = code;
		this.message = message;
	}

	private int code;

	private String message;

	public static ExceptionCode valueOfCode(int number) {
		return BY_LABEL.get(number);
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return message;
	}
}
