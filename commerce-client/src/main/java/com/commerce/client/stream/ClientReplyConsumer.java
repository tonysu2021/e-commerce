package com.commerce.client.stream;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.commerce.reactor.EventBus;
import com.commerce.stream.annotation.EventHandler;
import com.commerce.stream.constant.StreamActionType;
import com.commerce.stream.constant.StreamHeader;
import com.commerce.stream.dto.StreamMessageDTO;
import com.commerce.stream.protocol.StreamChannel;
import com.commerce.stream.store.StreamTransactionHelper;
import com.commerce.web.dto.event.CustomerDTO;

@Component("clientReplyConsumer")
public class ClientReplyConsumer {
	
	@Autowired
	@Qualifier("eventBus")
	private EventBus<CustomerDTO> eventSource;

	@Autowired
	@Qualifier("streamTransactionHelper")
	private StreamTransactionHelper<StreamMessageDTO<?>> helper;

	@Autowired
	@Qualifier("streamTransactionHelper")
	private StreamTransactionHelper<StreamMessageDTO<String>> orderHelper;

	@EventHandler(target = StreamChannel.CUSTOMER_BROADCAST_INPUT, eventType = "BROADCAST")
	public void receiveMsgFromBiz(@Header(name = StreamHeader.X_SERVER_ID, required = true) UUID serverId,
			@Header(name = StreamHeader.X_MESSAGE_ID, required = true) UUID messageId,
			@Header(name = StreamHeader.X_EVENT_TYPE, required = true) StreamActionType type,
			@Header(name = "deliveryAttempt", required = true) int attempt,
			@Payload StreamMessageDTO<CustomerDTO> payload) {
		helper.doInTransaction(StreamChannel.CUSTOMER_BROADCAST_INPUT, messageId, type, attempt, payload,
				item -> eventSource.dispathDataWith(payload.getMessage()));
	}

	@EventHandler(target = StreamChannel.ORDER_REPLY_INPUT, eventType = "TEST_REPLY")
	public void receiveMsgFromOrder(@Header(name = StreamHeader.X_SERVER_ID, required = true) UUID serverId,
			@Header(name = StreamHeader.X_MESSAGE_ID, required = true) UUID messageId,
			@Header(name = StreamHeader.X_EVENT_TYPE, required = true) StreamActionType type,
			@Header(name = "deliveryAttempt", required = true) int attempt, @Payload StreamMessageDTO<String> payload) {

		helper.doInTransaction(StreamChannel.ORDER_REPLY_INPUT, messageId, type, attempt, payload,item ->{});

	}

}
