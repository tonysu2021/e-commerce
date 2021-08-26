package com.commerce.web.client;

import java.time.Duration;
import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.commerce.web.exception.ResponseStatusCodeException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

public class WebClientTemplate {

	private static Logger logger = LoggerFactory.getLogger(WebClientTemplate.class);

	private Retry retry;

	private WebClient webClient;

	public WebClientTemplate(String baseUrl) {
		this.webClient = createWebClient(baseUrl);
		// 重試機制-使用背壓模式重試1次間隔500毫秒
		this.retry = Retry.backoff(1, Duration.ofMillis(500))
						.doAfterRetry(rs -> logger.debug("Retried at {}" , LocalTime.now()))
						.onRetryExhaustedThrow((spec, rs) -> new ResponseStatusCodeException(HttpStatus.NOT_FOUND));
	}

	private WebClient createWebClient(String baseUrl) {
		return WebClient.builder().baseUrl(baseUrl)
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}

	public <T> Flux<T> getWithFlux(String uri, Class<T> modelClz) {
		return webClient.get().uri(uri).retrieve().onStatus(HttpStatus::isError, clientResponse -> 
			Mono.error(new ResponseStatusCodeException(clientResponse.statusCode()))
		).bodyToFlux(modelClz).retryWhen(retry);
	}

	public <T> Mono<T> getWithMono(String uri, Class<T> modelClz) {
		return webClient.get().uri(uri).retrieve().onStatus(HttpStatus::isError, clientResponse -> 
			Mono.error(new ResponseStatusCodeException(clientResponse.statusCode()))
		).bodyToMono(modelClz).retryWhen(retry);
	}

	public <T> Mono<T> postWithMono(String uri, Object request, Class<?> requestClz, Class<T> modelClz) {
		return webClient.post().uri(uri).body(Mono.just(request), requestClz).retrieve()
				.onStatus(HttpStatus::isError, clientResponse -> 
					Mono.error(new ResponseStatusCodeException(clientResponse.statusCode()))
				).bodyToMono(modelClz).retryWhen(retry);
	}
	
	public <T> Mono<T> postWithMono(String uri,  Class<T> modelClz) {
		return webClient.post().uri(uri).retrieve()
				.onStatus(HttpStatus::isError, clientResponse -> 
					Mono.error(new ResponseStatusCodeException(clientResponse.statusCode()))
				).bodyToMono(modelClz).retryWhen(retry);
	}
	
	public <T> Mono<T> putWithMono(String uri, Object request, Class<?> requestClz, Class<T> modelClz) {
		return webClient.put().uri(uri).body(Mono.just(request), requestClz).retrieve()
				.onStatus(HttpStatus::isError, clientResponse -> 
					Mono.error(new ResponseStatusCodeException(clientResponse.statusCode()))
				).bodyToMono(modelClz).retryWhen(retry);
	}
	
	public <T> Mono<T> deleteWithMono(String uri, Class<T> modelClz) {
		return webClient.delete().uri(uri).retrieve().onStatus(HttpStatus::isError, clientResponse -> 
			Mono.error(new ResponseStatusCodeException(clientResponse.statusCode()))
		).bodyToMono(modelClz).retryWhen(retry);
	}
}
