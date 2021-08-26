package com.commerce.trading.stream;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.commerce.stream.constant.StreamHeader;
import com.commerce.stream.domain.StreamActionType;
import com.commerce.stream.domain.StreamMessage;
import com.commerce.stream.protocol.StreamChannel;

@Component("tradingConsumer")
public class TradingConsumer {
	private static final Logger LOGGER = LoggerFactory.getLogger(TradingConsumer.class);

	/**
	 * 接收到StreamType的Error訊息直接rollback customer跟order的資料。
	 * 
	 * 
	 */
	@RabbitListener(queues = { StreamChannel.CUSTOMER_GROUP_DLQ })
	public void processA(@Header(name = StreamHeader.X_MESSAGE_ID, required = true) UUID messageId,
			@Header(name = StreamHeader.X_EVENT_TYPE, required = true) StreamActionType type,
			@Header(StreamHeader.X_EXCEPTION_MSG_HEADER) String errorMsg, @Payload StreamMessage<String> payload) {

		LOGGER.info("[FailBack] Message id : {} ,type : {}, errorMsg : {} , payload : {} in customer", messageId, type,
				errorMsg, payload);
	}

	@RabbitListener(queues = { StreamChannel.ORDER_GROUP_DLQ })
	public void processB(@Header(name = StreamHeader.X_MESSAGE_ID, required = true) UUID messageId,
			@Header(name = StreamHeader.X_EVENT_TYPE, required = true) StreamActionType type,
			@Header(StreamHeader.X_EXCEPTION_MSG_HEADER) String errorMsg, @Payload StreamMessage<String> payload) {

		LOGGER.info("[FailBack] Message id : {} ,type : {}, errorMsg : {} , payload : {} in order", messageId, type,
				errorMsg, payload);
	}
	
	@RabbitListener(queues = { StreamChannel.ORDER_PRIORITY_GROUP_DLQ })
	public void processPB(@Header(name = StreamHeader.X_MESSAGE_ID, required = true) UUID messageId,
			@Header(name = StreamHeader.X_EVENT_TYPE, required = true) StreamActionType type,
			@Header(StreamHeader.X_EXCEPTION_MSG_HEADER) String errorMsg, @Payload StreamMessage<String> payload) {

		LOGGER.info("[FailBack] Message id : {} ,type : {}, errorMsg : {} , payload : {} in order priority", messageId, type,
				errorMsg, payload);
	}

	@RabbitListener(queues = { StreamChannel.PRODUCT_GROUP_DLQ })
	public void processC(@Header(name = StreamHeader.X_MESSAGE_ID, required = true) UUID messageId,
			@Header(name = StreamHeader.X_EVENT_TYPE, required = true) StreamActionType type,
			@Header(StreamHeader.X_EXCEPTION_MSG_HEADER) String errorMsg, @Payload StreamMessage<String> payload) {

		LOGGER.info("[FailBack] Message id : {} ,type : {}, errorMsg : {} , payload : {} in product", messageId, type,
				errorMsg, payload);
	}

}
