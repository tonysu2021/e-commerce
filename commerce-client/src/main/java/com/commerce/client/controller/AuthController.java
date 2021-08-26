package com.commerce.client.controller;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.common.domain.AuthDomain;
import com.auth.common.domain.AuthRequest;
import com.auth.common.service.AuthService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/auth")
@Validated
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<AuthDomain> login(@Validated @RequestBody AuthRequest request) {
		return authService.getToken(request.getUserName(), request.getPassword());
	}

	@PostMapping(value = "/refreshToken", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<AuthDomain> refreshToken(@RequestHeader(value = "Authorization") String token,
			@NotBlank @RequestHeader(value = "x-username") String userName) {
		return authService.refreshToken(userName, token);
	}
	
	@PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Boolean> logout(@NotBlank @RequestHeader(value = "x-username") String userName) {
		return authService.logout(userName);
	}
}
