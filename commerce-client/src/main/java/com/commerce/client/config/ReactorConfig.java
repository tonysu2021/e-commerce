package com.commerce.client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.commerce.reactor.EventSource;
import com.commerce.reactor.processors.CustomEventProcessor;
import com.commerce.web.client.CustomerClient;
import com.commerce.web.client.WebClientTemplate;

@Configuration
public class ReactorConfig {

	private static final String BASE_URL = "https://streaming-graph.facebook.com";
	
	private static final String BIZ_URL = "http://127.0.0.1:10079/commerce-biz-customer/customer";

	@Bean("eventSource")
	public <T> EventSource<T> eventSource() {
		return new EventSource<>(new CustomEventProcessor<>());
	}

	/**
	 * 目的取代FeignClient的功能。
	 * 
	 */
	@Bean("fbWebClient")
	public WebClientTemplate webClientTemplate() {
		return new WebClientTemplate(BASE_URL);
	}
	
	@Bean("customerClient")
	public CustomerClient customerClient() {
		return new CustomerClient(BIZ_URL);
	}
}
