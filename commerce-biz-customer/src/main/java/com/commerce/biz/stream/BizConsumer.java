package com.commerce.biz.stream;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.ImmediateAcknowledgeAmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.commerce.stream.annotation.EventHandler;
import com.commerce.stream.constant.StreamActionType;
import com.commerce.stream.constant.StreamHeader;
import com.commerce.stream.dto.StreamMessageDTO;
import com.commerce.stream.exception.ExceptionCodeEnum;
import com.commerce.stream.protocol.StreamChannel;
import com.commerce.stream.store.StreamTransactionHelper;

@Component("bizConsumer")
public class BizConsumer {
	private static final Logger LOGGER = LoggerFactory.getLogger(BizConsumer.class);

	@Autowired
	@Qualifier("streamTransactionHelper")
	private StreamTransactionHelper<StreamMessageDTO<String>> helper;

	@EventHandler(target = StreamChannel.CUSTOMER_INPUT, eventType = "TEST")
	public void receiveMsgFromCustomer(@Header(name = StreamHeader.X_SERVER_ID, required = true) UUID serverId,
			@Header(name = StreamHeader.X_MESSAGE_ID, required = true) UUID messageId,
			@Header(name = StreamHeader.X_EVENT_TYPE, required = true) StreamActionType type,
			@Header(name = "deliveryAttempt", required = true) int attempt, @Payload StreamMessageDTO<String> payload) {

		helper.doInTransaction(StreamChannel.CUSTOMER_INPUT, messageId, type, attempt, payload, item -> {
			String msg = item.getMessage();
			if (StringUtils.contains(msg, "error")) {
				throw new AmqpRejectAndDontRequeueException(ExceptionCodeEnum.DOMAIN_ERROR.getMsg());
			}
			if (StringUtils.contains(msg, "pass-a")) {
				throw new ImmediateAcknowledgeAmqpException(ExceptionCodeEnum.DOMAIN_ERROR.getMsg());
			}
		});
	}

	@EventHandler(target = StreamChannel.CUSTOMER_INPUT, eventType = "NORMAL")
	public void receiveMsgFromClient(@Header(name = StreamHeader.X_SERVER_ID, required = true) UUID serverId,
			@Header(name = StreamHeader.X_MESSAGE_ID, required = true) UUID messageId,
			@Header(name = StreamHeader.X_EVENT_TYPE, required = true) StreamActionType type,
			@Header(name = "deliveryAttempt", required = true) int attempt, @Payload StreamMessageDTO<String> payload) {

		LOGGER.info("[Biz-Consumer] Received Message id :{} , type : {} , body: {} from Client.", messageId, type,
				payload);
	}
}