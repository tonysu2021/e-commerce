package com.commerce.client.controller;

import java.io.Serializable;
import java.time.Duration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.client.service.CustomerService;
import com.commerce.client.stream.ClientProducer;
import com.commerce.stream.constant.StreamActionType;
import com.commerce.stream.dto.StreamMessageDTO;
import com.commerce.web.dto.event.CustomerDTO;
import com.commerce.web.dto.request.CustomerPostRequest;
import com.commerce.web.dto.request.CustomerPutRequest;
import com.commerce.web.dto.request.StreamRequest;
import com.commerce.web.exception.ExceptionCode;
import com.commerce.web.exception.ResponseStatusCodeException;
import com.commerce.web.validator.IdFormat;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/customer")
@Validated
public class CustomerController {

	@Autowired
	@Qualifier("clientProducer")
	private ClientProducer producer;
	
	@Autowired
	@Qualifier("customerService")
	protected CustomerService customerService;

	@GetMapping(value = "/{customerId}")
	public Mono<CustomerDTO> getCustomer(@PathVariable String customerId) {
		return customerService.findByAppId(customerId)
				.switchIfEmpty(Mono.error(new ResponseStatusCodeException(HttpStatus.NO_CONTENT)));
	}

	@GetMapping
	public Flux<CustomerDTO> getCustomerList() {
		return customerService.findAll();
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<CustomerDTO> addCustomer(@Validated @RequestBody CustomerPostRequest request) {
		return customerService.findByAppId(request.getCustomerId())
				.map((CustomerDTO domain) -> {
					if(StringUtils.isNotEmpty(domain.getCustomerId())) {
						throw new ResponseStatusCodeException(HttpStatus.PRECONDITION_FAILED,ExceptionCode.CUSTOMER_EXISTED);
					}
					return domain;
				}).switchIfEmpty(customerService.saveCustomer(request));
	}
	
	@PutMapping(value = "/{customerId}")
	public Mono<CustomerDTO> updateCustomer(@IdFormat @PathVariable String customerId,
			@Validated @RequestBody CustomerPutRequest request){
		return customerService.findByAppId(customerId)
				.switchIfEmpty(Mono.error(new ResponseStatusCodeException(HttpStatus.PRECONDITION_FAILED,ExceptionCode.CUSTOMER_NOT_EXIST)))
				.flatMap(domain -> customerService.updateCustomer(customerId,request));
	}
	
	@DeleteMapping(value = "/{customerId}")
	public Mono<Void> deleteCustomer(@IdFormat @PathVariable String customerId){
		return customerService.findByAppId(customerId)
				.switchIfEmpty(Mono.error(new ResponseStatusCodeException(HttpStatus.PRECONDITION_FAILED,ExceptionCode.CUSTOMER_NOT_EXIST)))
				.flatMap(domain -> customerService.deleteCustomer(customerId));
	}

	@GetMapping(value = "/events/{rate}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<? extends Serializable>> events(@PathVariable long rate) {
		Flux<CustomerDTO> events = customerService.getEvent().delayElements(Duration.ofSeconds(rate)).log("events");
		
		Flux<ServerSentEvent<CustomerDTO>> sseData = events.map(event -> 
			ServerSentEvent.builder(event).event(event.getEventType().getDesc()).build());
		Flux<ServerSentEvent<String>> ping = Flux.interval(Duration.ofSeconds(rate * 2))
				.map(l -> ServerSentEvent.builder("").event("ping").data("").build());
		return Flux.merge(sseData, ping);
	}
	
	@GetMapping(value = "/apiEvents/{rate}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<ServerSentEvent<CustomerDTO>> apiEvents(@PathVariable long rate) {
		Flux<CustomerDTO> events = customerService.getEvent().delayElements(Duration.ofSeconds(rate)).log("apiEvents");
		
		Flux<ServerSentEvent<CustomerDTO>> sseData = events.map(event -> 
			ServerSentEvent.builder(event).event("data").build());

		return sseData.timeout(Duration.ofSeconds(60))
				.onErrorReturn(ServerSentEvent.builder(new CustomerDTO()).event("close").build());
	}
	
	@PostMapping(value = "/{type}")
	public ResponseEntity<Mono<String>> normal(@PathVariable String type,
			@RequestBody StreamRequest request) {
		StreamActionType streamType = StreamActionType.getInstanceOf(type);
		if (streamType == null) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Mono.just("no type"));
		}
		
		StreamMessageDTO<String> message = new StreamMessageDTO<>();
		message.setMessage(request.getPayload());
		producer.sendToCustomer(streamType,message);
		producer.sendToOrder(streamType,message);
		producer.sendToOrderP(streamType,message);
		producer.sendToProduct(streamType,message);
		return ResponseEntity.status(HttpStatus.OK).body(Mono.just("success"));
	}

}
