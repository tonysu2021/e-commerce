package com.commerce.biz.stream;

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
import com.commerce.stream.constant.StreamActionType;
import com.commerce.stream.constant.StreamHeader;
import com.commerce.stream.dto.StreamMessageDTO;
import com.commerce.stream.protocol.StreamChannel;

@Component("bizReplyProducer")
public class BizReplyProducer {
	private static final Logger LOGGER = LoggerFactory.getLogger(BizReplyProducer.class);

	@Autowired
	@Qualifier("serverInfo")
	private ServerInfo serverInfo;
	
	@Autowired
	@Qualifier(StreamChannel.CUSTOMER_BROADCAST_OUTPUT)
	private MessageChannel msgBizChannel;


	
	public <T> void sendToCustomerBroadcast(StreamMessageDTO<T> message) {
		UUID uuid = UUID.randomUUID();
		LOGGER.info("[Biz-Reply-Producer] Sending Message ({}) {} to Client from customer",uuid, message);
		msgBizChannel.send(MessageBuilder.withPayload(message)
				.setHeader(StreamHeader.X_SERVER_ID, serverInfo.getServerId())
				.setHeader(StreamHeader.X_MESSAGE_ID, uuid)
				.setHeader(StreamHeader.X_EVENT_TYPE, StreamActionType.BROADCAST)
				.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());
	}

}