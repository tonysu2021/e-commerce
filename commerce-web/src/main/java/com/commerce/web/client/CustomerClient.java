package com.commerce.web.client;

import org.springframework.cloud.openfeign.support.SpringMvcContract;

import com.commerce.web.domain.CustomerDomain;
import com.commerce.web.protocol.CustomerProtocol;
import com.commerce.web.request.CustomerPostRequest;
import com.commerce.web.request.CustomerPutRequest;
import com.commerce.web.webclient.WebReactiveFeign;

import reactivefeign.ReactiveContract;
import reactivefeign.retry.BasicReactiveRetryPolicy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CustomerClient {

	private CustomerProtocol protocol;

	public CustomerClient(String url) {
		super();
		this.protocol = WebReactiveFeign.<CustomerProtocol>builder()
				.retryWhen(BasicReactiveRetryPolicy.retryWithBackoff(3, 500))
				.contract(new ReactiveContract(new SpringMvcContract())).target(CustomerProtocol.class, url);
	}

	public Mono<CustomerDomain> getCustomer(String customerId) {
		return protocol.getCustomer(customerId);
	}

	public Flux<CustomerDomain> getCustomerList() {
		return protocol.getCustomerList();
	}

	public Mono<CustomerDomain> addCustomer(CustomerPostRequest request) {
		return protocol.addCustomer(request);
	}

	public Mono<CustomerDomain> updateCustomer(String customerId, CustomerPutRequest request) {
		return protocol.updateCustomer(customerId, request);
	}

	public Mono<Void> deleteCustomer(String customerId) {
		return protocol.deleteCustomer(customerId);
	}
}
