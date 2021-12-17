package com.commerce.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.commerce.reactor.EventSource;
import com.commerce.reactor.processors.CustomEventProcessor;
import com.commerce.web.protocol.CustomerProtocol;
import com.commerce.web.webclient.ProtocolFactory;

@Configuration
public class ReactorConfig {

	private static final String BIZ_URL = "http://commerce-biz-customer:10079/api/customer";

	@Bean("eventSource")
	public <T> EventSource<T> eventSource() {
		return new EventSource<>(new CustomEventProcessor<>());
	}

	/**
	 * 目的取代FeignClient的功能。
	 * 
	 */
	@Bean("customerProtocol")
	public CustomerProtocol customerProtocol() {
		return ProtocolFactory.createProtocol(BIZ_URL, CustomerProtocol.class);
	}
}
