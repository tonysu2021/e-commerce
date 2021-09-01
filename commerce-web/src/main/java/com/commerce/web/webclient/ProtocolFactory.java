package com.commerce.web.webclient;

import org.springframework.cloud.openfeign.support.SpringMvcContract;

import reactivefeign.ReactiveContract;
import reactivefeign.retry.BasicReactiveRetryPolicy;

public class ProtocolFactory {
	private ProtocolFactory() {
	}

	public static <T> T createProtocol(String url, Class<T> clz) {
		return createProtocol(url, clz, Boolean.FALSE);
	}

	public static <T> T createProtocol(String url, Class<T> clz, Boolean useSSL) {
		return WebReactiveFeign.<T>builder(useSSL)
				.retryWhen(BasicReactiveRetryPolicy.retryWithBackoff(3, 500))
				.contract(new ReactiveContract(new SpringMvcContract()))
				.target(clz, url);
	}
}
