package com.commerce.biz.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.commerce.biz.entity.Customer;

import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, String> {

	@Query("Select * From cloud.customer customer Where customer.customer_id = :customerId "
			+ "And (:enabled is null or customer.enabled = :enabled) ")
	public Mono<Customer> findByAppId(String customerId,Boolean enabled);
	
}
