package com.commerce.trading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.commerce.cache.EnableCache;

@SpringBootApplication
@EnableCache
public class CommerceTradingApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommerceTradingApplication.class, args);
	}

}
