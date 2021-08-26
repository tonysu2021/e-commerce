package com.commerce.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.client.service.LiveComService;
import com.commerce.web.request.LiveComRequest;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/lives")
public class LiveComController {

	@Autowired
	protected LiveComService liveComService;

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Mono<String>> createLiveCom(@RequestBody LiveComRequest request) {

		liveComService.createLiveCom(request.getLiveId(), request.getAccessToken());
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(Mono.just("accepted"));
	}

	@DeleteMapping(value = "/{liveId}")
	public ResponseEntity<Mono<String>> closeLiveCom(@PathVariable String liveId) {
		liveComService.closeLiveCom(liveId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Mono.just("no content"));
	}
}
