package com.commerce.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.auth.common.annotation.EnableAuth;
import com.commerce.cache.EnableCache;
import com.commerce.web.annotation.EnableServiceCommon;

@SpringBootApplication
@EnableCache
@EnableAuth
@EnableServiceCommon
public class CommerceClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommerceClientApplication.class, args);
	}

}
