package com.commerce.txn.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.commerce.web.util.SnowflakeIdWorker;

@Configuration
public class SnowflakeConfig {

	private static Logger logger = LoggerFactory.getLogger(SnowflakeConfig.class);

	@Bean
	public SnowflakeIdWorker snowflakeIdWorker(SnowflakeConfigProp prop) {
		logger.info("Snowflake start. datacenter id : {}, worker id : {}", prop.getDatacenterId(), prop.getWorkerId());
		return new SnowflakeIdWorker(prop.getDatacenterId(), prop.getWorkerId());
	}
}
