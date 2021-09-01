package com.commerce.web.protocol;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.commerce.web.dto.CustomerDTO;
import com.commerce.web.request.CustomerPostRequest;
import com.commerce.web.request.CustomerPutRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerProtocol {

	@GetMapping(value = "/{customerId}")
	public Mono<CustomerDTO> getCustomer(@PathVariable String customerId);

	@GetMapping
	public Flux<CustomerDTO> getCustomerList();

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<CustomerDTO> addCustomer(@RequestBody CustomerPostRequest request);

	@PutMapping(value = "/{customerId}")
	public Mono<CustomerDTO> updateCustomer(@PathVariable String customerId,
			@RequestBody CustomerPutRequest request);

	@DeleteMapping(value = "/{customerId}")
	public Mono<Void> deleteCustomer(@PathVariable String customerId);
}
