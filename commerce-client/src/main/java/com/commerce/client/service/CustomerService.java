package com.commerce.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.commerce.cache.client.CacheManager;
import com.commerce.reactor.EventSource;
import com.commerce.web.client.CustomerClient;
import com.commerce.web.domain.CustomerDomain;
import com.commerce.web.request.CustomerPostRequest;
import com.commerce.web.request.CustomerPutRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("customerService")
public class CustomerService {

	@Autowired
	private CacheManager<CustomerDomain> cacheManager;

	@Autowired
	@Qualifier("customerClient")
	private CustomerClient customerClient;

	@Autowired
	@Qualifier("eventSource")
	private EventSource<CustomerDomain> eventSource;

	public Flux<CustomerDomain> findAll() {
		return customerClient.getCustomerList();
	}

	public Mono<CustomerDomain> findByAppId(String customerId) {
		return cacheManager.get(customerId, CustomerDomain.class)
				.switchIfEmpty(customerClient.getCustomer(customerId)
						.flatMap(data -> cacheManager.save(data.getCustomerId(), data).map(i -> data))
					);
	}
	
	public Mono<CustomerDomain> saveCustomer(CustomerPostRequest request) {
		return customerClient.addCustomer(request);
	}
	
	public Mono<CustomerDomain> updateCustomer(String customerId,CustomerPutRequest request){
		return customerClient.updateCustomer(customerId, request);
	}
	
	public Mono<Void> deleteCustomer(String customerId){
		return customerClient.deleteCustomer(customerId);
	}

	public Flux<CustomerDomain> getEvent() {
		return eventSource.getFlux();
	}
}
