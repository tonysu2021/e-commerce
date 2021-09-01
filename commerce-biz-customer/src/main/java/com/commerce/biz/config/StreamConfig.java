package com.commerce.biz.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.commerce.cache.client.CacheManager;
import com.commerce.stream.annotation.EnableCustomMQ;
import com.commerce.stream.annotation.EnableEventHandling;
import com.commerce.stream.protocol.StreamProtocols;
import com.commerce.stream.store.StreamRecordPO;
import com.commerce.stream.store.StreamStorage;
import com.commerce.stream.store.StreamTransactionHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SuppressWarnings("deprecation")
@Configuration
@EnableBinding(value = { StreamProtocols.class })
@EnableEventHandling
@EnableCustomMQ
public class StreamConfig {

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		return mapper;
	}

	@Bean
	public StreamStorage<StreamRecordPO> streamStorage(CacheManager<StreamRecordPO> cacheManager) {
		return new StreamStorage<StreamRecordPO>() {

			@Override
			public StreamRecordPO get(String key, Class<StreamRecordPO> clz) {
				return cacheManager.get(key, clz).block();
			}

			@Override
			public Boolean save(String key, StreamRecordPO data) {
				return cacheManager.save(key, data).block();
			}
		};
	}

	@Bean("streamTransactionHelper")
	public <T> StreamTransactionHelper<T> streamTransactionHelper(StreamStorage<StreamRecordPO> streamStorage,
			ObjectMapper mapper) {
		return new StreamTransactionHelper<>(streamStorage, mapper);
	}

}
