package com.commerce.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.commerce.cache.client.CacheManager;
import com.commerce.reactor.EventBus;
import com.commerce.web.dto.event.CustomerDTO;
import com.commerce.web.dto.request.CustomerPostRequest;
import com.commerce.web.dto.request.CustomerPutRequest;
import com.commerce.web.protocol.CustomerProtocol;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("customerService")
public class CustomerService {

	@Autowired
	private CacheManager<CustomerDTO> cacheManager;

	@Autowired
	@Qualifier("customerProtocol")
	private CustomerProtocol protocol;

	@Autowired
	@Qualifier("eventBus")
	private EventBus<CustomerDTO> eventSource;

	public Flux<CustomerDTO> findAll() {
		return protocol.getCustomerList();
	}

	public Mono<CustomerDTO> findByAppId(String customerId) {
		return cacheManager.get(customerId, CustomerDTO.class)
				.switchIfEmpty(protocol.getCustomer(customerId)
						.flatMap(data -> cacheManager.save(data.getCustomerId(), data).map(i -> data))
					);
	}
	
	public Mono<CustomerDTO> saveCustomer(CustomerPostRequest request) {
		return protocol.addCustomer(request);
	}
	
	public Mono<CustomerDTO> updateCustomer(String customerId,CustomerPutRequest request){
		return protocol.updateCustomer(customerId, request);
	}
	
	public Mono<Void> deleteCustomer(String customerId){
		return protocol.deleteCustomer(customerId);
	}

	public Flux<CustomerDTO> getEvent() {
		return eventSource.getFlux();
	}
}
