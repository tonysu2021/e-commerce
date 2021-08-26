package com.commerce.biz.stream;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.ImmediateAcknowledgeAmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.commerce.stream.annotation.EventHandler;
import com.commerce.stream.constant.StreamHeader;
import com.commerce.stream.domain.StreamActionType;
import com.commerce.stream.domain.StreamMessage;
import com.commerce.stream.exception.ExceptionCodeEnum;
import com.commerce.stream.protocol.StreamChannel;
import com.commerce.stream.store.StreamTransactionHelper;

@Component("bizConsumer")
public class BizConsumer {
	
	@Autowired
	@Qualifier("streamTransactionHelper")
	private StreamTransactionHelper<StreamMessage<String>> helper;

	@EventHandler(target = StreamChannel.PRODUCT_INPUT, eventType = "TEST")
	public void receiveMsgFromProduct(@Header(name = StreamHeader.X_SERVER_ID, required = true) UUID serverId,
			@Header(name = StreamHeader.X_MESSAGE_ID, required = true) UUID messageId,
			@Header(name = StreamHeader.X_EVENT_TYPE, required = true) StreamActionType type,
			@Header(name = "deliveryAttempt", required = true) int attempt, @Payload StreamMessage<String> payload) {

		helper.doInTransaction(StreamChannel.PRODUCT_INPUT, messageId, type, attempt, payload, item -> {
			String msg = item.getMessage();
			if (StringUtils.contains(msg, "error")) {
				throw new AmqpRejectAndDontRequeueException(ExceptionCodeEnum.DOMAIN_ERROR.getMsg());
			}
			if (StringUtils.contains(msg, "pass-c")) {
				throw new ImmediateAcknowledgeAmqpException(ExceptionCodeEnum.DOMAIN_ERROR.getMsg());
			}
		});
	}
}