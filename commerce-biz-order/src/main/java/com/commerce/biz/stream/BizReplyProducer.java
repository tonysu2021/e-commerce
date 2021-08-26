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

import com.commerce.stream.constant.StreamHeader;
import com.commerce.stream.domain.StreamActionType;
import com.commerce.stream.domain.StreamMessage;
import com.commerce.stream.protocol.StreamChannel;

@Component("bizReplyProducer")
public class BizReplyProducer {
	private static final Logger LOGGER = LoggerFactory.getLogger(BizReplyProducer.class);

	@Autowired
	@Qualifier(StreamChannel.ORDER_REPLY_OUTPUT)
	private MessageChannel msgOrderReplyChannel;

	public <T> void sendToOrderReply(UUID serverId, UUID messageId, StreamActionType streamType,
			StreamMessage<T> message) {
		LOGGER.info("[Biz-Reply-Producer] Received Server id :{},Message id :{} , type : {}  from order.",
				serverId, messageId, streamType);
		msgOrderReplyChannel.send(MessageBuilder.withPayload(message)
				.setHeader(StreamHeader.X_SERVER_ID, serverId)
				.setHeader(StreamHeader.X_MESSAGE_ID, messageId)
				.setHeader(StreamHeader.X_EVENT_TYPE, streamType)
				.setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());
	}

}