package com.commerce.biz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.commerce.cache.EnableCache;

@SpringBootApplication
@EnableCache
public class CommerceBizApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommerceBizApplication.class, args);
	}

}
