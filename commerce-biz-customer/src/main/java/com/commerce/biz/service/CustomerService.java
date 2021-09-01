package com.commerce.biz.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.commerce.biz.entity.CustomerEntity;
import com.commerce.biz.repository.CustomerRepository;
import com.commerce.biz.stream.BizReplyProducer;
import com.commerce.biz.util.CommonUtils;
import com.commerce.cache.client.CacheManager;
import com.commerce.reactor.EventType;
import com.commerce.stream.dto.StreamMessageDTO;
import com.commerce.web.dto.CustomerDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("customerService")
public class CustomerService {

	@Autowired
	@Qualifier("bizReplyProducer")
	private BizReplyProducer producer;

	@Autowired
	private CacheManager<CustomerDTO> cacheManager;

	@Autowired
	private CustomerRepository customerAppRepository;

	public Flux<CustomerEntity> findAll() {
		return customerAppRepository.findAll();
	}

	public Mono<CustomerEntity> findByAppId(String customerId) {
		return customerAppRepository.findByAppId(customerId, null);
	}

	public Mono<CustomerEntity> saveCustomer(String customerId, String name, String email ,String createBy) {
		CustomerEntity customer = new CustomerEntity();
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

	public Mono<CustomerEntity> updateCustomer(CustomerEntity customer, String modifyBy) {
		customer.setModifyTime(Instant.now());
		customer.setModifyBy(modifyBy);
		return customerAppRepository.save(customer)
				.doOnSuccess(data -> cacheManager.delete(customer.getCustomerId(), CustomerDTO.class).subscribe())
				// Send the latest information to RabbitMq
				.doOnSuccess(data -> sendMsg(data,EventType.DATA_UPATE));
	}
	
	private void sendMsg(CustomerEntity data,EventType eventType) {
		CustomerDTO domain = CommonUtils.convert(data);
		domain.setEventType(eventType);
		StreamMessageDTO<CustomerDTO> message = new StreamMessageDTO<>();
		message.setMessage(domain);
		producer.sendToCustomerBroadcast(message);
	}

	public Mono<CustomerEntity> disabledCustomer(CustomerEntity customer) {
		customer.setEnabled(Boolean.FALSE);
		return customerAppRepository.save(customer);
	}

	public Mono<Void> deleteCustomer(CustomerEntity customer) {
		return customerAppRepository.delete(customer)
				.doOnSuccess(data -> cacheManager.delete(customer.getCustomerId(), CustomerDTO.class).subscribe())
				.doOnSuccess(data -> sendMsg(customer,EventType.DATA_DELETE));
	}
}
