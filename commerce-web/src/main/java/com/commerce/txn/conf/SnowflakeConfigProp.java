package com.commerce.txn.conf;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "snowflake")
@AutoConfigureAfter(SnowflakeConfig.class)
public class SnowflakeConfigProp {
	
	private long datacenterId;
	
	private long workerId;
	
	public long getDatacenterId() {
		return datacenterId;
	}
	public void setDatacenterId(long datacenterId) {
		this.datacenterId = datacenterId;
	}
	public long getWorkerId() {
		return workerId;
	}
	public void setWorkerId(long workerId) {
		this.workerId = workerId;
	}
	
}
