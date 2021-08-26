package com.commerce.biz.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.commerce.biz.entity.Customer;
import com.commerce.biz.repository.CustomerRepository;
import com.commerce.biz.stream.BizReplyProducer;
import com.commerce.biz.util.CommonUtils;
import com.commerce.cache.client.CacheManager;
import com.commerce.reactor.EventType;
import com.commerce.stream.domain.StreamMessage;
import com.commerce.web.domain.CustomerDomain;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("customerService")
public class CustomerService {

	@Autowired
	@Qualifier("bizReplyProducer")
	private BizReplyProducer producer;

	@Autowired
	private CacheManager<CustomerDomain> cacheManager;

	@Autowired
	private CustomerRepository customerAppRepository;

	public Flux<Customer> findAll() {
		return customerAppRepository.findAll();
	}

	public Mono<Customer> findByAppId(String customerId) {
		return customerAppRepository.findByAppId(customerId, null);
	}

	public Mono<Customer> saveCustomer(String customerId, String name, String email ,String createBy) {
		Customer customer = new Customer();
		customer.setCustomerId(customerId);
		customer.setName(name);
		customer.setEmail(email);
		customer.setEnabled(Boolean.TRUE);
		customer.setCreateTime(Instant.now());
		customer.setCreateBy(createBy);
		customer.setFromIp("localhost");
		return customerAppRepository.save(customer.setAsNew())
				.doOnSuccess(data -> sendMsg(data,EventType.DATA_CREATE));
	}

	public Mono<Customer> updateCustomer(Customer customer, String modifyBy) {
		customer.setModifyTime(Instant.now());
		customer.setModifyBy(modifyBy);
		return customerAppRepository.save(customer)
				.doOnSuccess(data -> cacheManager.delete(customer.getCustomerId(), CustomerDomain.class).subscribe())
				// Send the latest information to RabbitMq
				.doOnSuccess(data -> sendMsg(data,EventType.DATA_UPATE));
	}
	
	private void sendMsg(Customer data,EventType eventType) {
		CustomerDomain domain = CommonUtils.convert(data);
		domain.setEventType(eventType);
		StreamMessage<CustomerDomain> message = new StreamMessage<>();
		message.setMessage(domain);
		producer.sendToCustomerBroadcast(message);
	}

	public Mono<Customer> disabledCustomer(Customer customer) {
		customer.setEnabled(Boolean.FALSE);
		return customerAppRepository.save(customer);
	}

	public Mono<Void> deleteCustomer(Customer customer) {
		return customerAppRepository.delete(customer)
				.doOnSuccess(data -> cacheManager.delete(customer.getCustomerId(), CustomerDomain.class).subscribe())
				.doOnSuccess(data -> sendMsg(customer,EventType.DATA_DELETE));
	}
}
