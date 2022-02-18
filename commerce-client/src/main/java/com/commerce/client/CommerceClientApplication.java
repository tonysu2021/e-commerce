package com.commerce.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.auth.common.annotation.EnableAuth;
import com.commerce.cache.EnableCache;
import com.commerce.txn.annotation.EnableSnowflake;
import com.commerce.web.annotation.EnableCommonService;

@SpringBootApplication
@EnableCache
@EnableAuth
@EnableCommonService
@EnableSnowflake
public class CommerceClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommerceClientApplication.class, args);
	}

}
