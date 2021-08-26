package com.commerce.web.client;

import com.commerce.web.domain.CustomerDomain;
import com.commerce.web.protocol.CustomerProtocol;
import com.commerce.web.request.CustomerPostRequest;
import com.commerce.web.request.CustomerPutRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class CustomerClient implements CustomerProtocol {

	private WebClientTemplate webClientTemplate;
	
	public CustomerClient(WebClientTemplate webClientTemplate) {
		super();
		this.webClientTemplate = webClientTemplate;
	}

	@Override
	public Mono<CustomerDomain> getCustomer(String customerId) {
		return webClientTemplate.getWithMono("/" + customerId, CustomerDomain.class);
	}

	@Override
	public Flux<CustomerDomain> getCustomerList() {
		return webClientTemplate.getWithFlux("", CustomerDomain.class);
	}

	@Override
	public Mono<CustomerDomain> addCustomer(CustomerPostRequest request) {
		return webClientTemplate.postWithMono("", request, CustomerPostRequest.class, CustomerDomain.class);
	}

	@Override
	public Mono<CustomerDomain> updateCustomer(String customerId, CustomerPutRequest request) {
		return webClientTemplate.putWithMono("/" + customerId, request, CustomerPutRequest.class,
				CustomerDomain.class);
	}

	@Override
	public Mono<Void> deleteCustomer(String customerId) {
		return webClientTemplate.deleteWithMono("/" + customerId, Void.class);
	}

}
