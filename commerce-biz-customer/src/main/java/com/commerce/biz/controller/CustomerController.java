package com.commerce.biz.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.biz.service.CustomerService;
import com.commerce.biz.util.CommonUtils;
import com.commerce.web.dto.event.CustomerDTO;
import com.commerce.web.dto.request.CustomerPostRequest;
import com.commerce.web.dto.request.CustomerPutRequest;
import com.commerce.web.exception.ExceptionCode;
import com.commerce.web.exception.ResponseStatusCodeException;
import com.commerce.web.protocol.CustomerProtocol;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/customer")
public class CustomerController implements CustomerProtocol {

	@Autowired
	@Qualifier("customerService")
	protected CustomerService customerService;

	@Override
	public Mono<CustomerDTO> getCustomer(@PathVariable String customerId) {
		return customerService.findByAppId(customerId).map(CommonUtils::convert);
	}

	@Override
	public Flux<CustomerDTO> getCustomerList() {
		return customerService.findAll().map(CommonUtils::convert);
	}

	@Override
	public Mono<CustomerDTO> addCustomer(@RequestBody CustomerPostRequest request) {
		return customerService
				.saveCustomer(request.getCustomerId(), request.getName(), request.getEmail(), request.getCustomerId())
				.map(CommonUtils::convert);
	}

	@Override
	public Mono<CustomerDTO> updateCustomer(String customerId, CustomerPutRequest request) {
		return customerService.findByAppId(customerId)
				.switchIfEmpty(Mono.error(
						new ResponseStatusCodeException(HttpStatus.PRECONDITION_FAILED, ExceptionCode.CUSTOMER_NOT_EXIST)))
				.flatMap(domain -> {
					domain.setName(request.getName());
					return customerService.updateCustomer(domain, domain.getCustomerId());
				}).map(CommonUtils::convert);
	}

	@Override
	public Mono<Void> deleteCustomer(String customerId) {
		return customerService.findByAppId(customerId)
				.switchIfEmpty(Mono.error(
						new ResponseStatusCodeException(HttpStatus.PRECONDITION_FAILED, ExceptionCode.CUSTOMER_NOT_EXIST)))
				.flatMap(domain -> customerService.deleteCustomer(domain));
	}

}
