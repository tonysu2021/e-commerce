package com.commerce.client.stream;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import com.commerce.stream.conf.ServerInfo;
import com.commerce.stream.constant.StreamHeader;
import com.commerce.stream.domain.StreamActionType;
import com.commerce.stream.domain.StreamMessage;
import com.commerce.stream.protocol.StreamChannel;

@Component("clientProducer")
public class ClientProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientProducer.class);

	@Autowired
	@Qualifier("serverInfo")
	private ServerInfo serverInfo;
	
	@Autowired
	@Qualifier(StreamChannel.CUSTOMER_OUTPUT)
	private MessageChannel msgCustomerChannel;
	
	@Autowired
	@Qualifier(StreamChannel.ORDER_OUTPUT)
	private MessageChannel msgOrderChannel;
	
	@Autowired
	@Qualifier(StreamChannel.ORDER_PRIORITY_OUTPUT)
	private MessageChannel msgOrderPChannel;
	
	@Autowired
	@Qualifier(StreamChannel.PRODUCT_OUTPUT)
	private MessageChannel msgProductChannel;
	
	public <T> void sendToCustomer(StreamActionType streamType,StreamMessage<T> message) {
		LOGGER.info("[Client-Producer] Sending Message {} to customer", message);
		msgCustomerChannel.send(MessageBuilder.withPayload(message)
				.setHeader(StreamHeader.X_SERVER_ID, serverInfo.getServerId())
				.setHeader(StreamHeader.X_MESSAGE_ID, UUID.randomUUID())
				.setHeader(StreamHeader.X_EVENT_TYPE, streamType)
				.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());
	}
	
	public <T> void sendToOrder(StreamActionType streamType,StreamMessage<T> message) {
		LOGGER.info("[Client-Producer] Sending Message {} to order", message);
		msgOrderChannel.send(MessageBuilder.withPayload(message)
				.setHeader(StreamHeader.X_SERVER_ID, serverInfo.getServerId())
				.setHeader(StreamHeader.X_MESSAGE_ID, UUID.randomUUID())
				.setHeader(StreamHeader.X_EVENT_TYPE, streamType)
				.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());
	}
	
	public <T> void sendToOrderP(StreamActionType streamType,StreamMessage<T> message) {
		LOGGER.info("[Client-Producer] Sending Message {} to order priority", message);
		msgOrderPChannel.send(MessageBuilder.withPayload(message)
				.setHeader(StreamHeader.X_SERVER_ID, serverInfo.getServerId())
				.setHeader(StreamHeader.X_MESSAGE_ID, UUID.randomUUID())
				.setHeader(StreamHeader.X_EVENT_TYPE, streamType)
				.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());
	}
	
	public <T> void sendToProduct(StreamActionType streamType,StreamMessage<T> message) {
		LOGGER.info("[Client-Producer] Sending Message {} to product", message);
		msgProductChannel.send(MessageBuilder.withPayload(message)
				.setHeader(StreamHeader.X_SERVER_ID, serverInfo.getServerId())
				.setHeader(StreamHeader.X_MESSAGE_ID, UUID.randomUUID())
				.setHeader(StreamHeader.X_EVENT_TYPE, streamType)
				.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());
	}
}
