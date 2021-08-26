package com.commerce.web.webclient;

import static com.commerce.web.webclient.NettyClientHttpConnectorBuilder.buildNettyClientHttpConnector;
import static reactivefeign.webclient.client.WebReactiveHttpClient.webClient;

import java.util.function.BiFunction;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;

import reactivefeign.ReactiveFeign;
import reactivefeign.ReactiveOptions;
import reactivefeign.client.ReactiveHttpRequest;
import reactivefeign.client.ReadTimeoutException;
import reactivefeign.webclient.CustomizableWebClientBuilder;
import reactivefeign.webclient.WebClientFeignCustomizer;

public class WebReactiveFeign {
	
	private WebReactiveFeign() {}

    public static <T> Builder<T> builder() {
        return builder(WebClient.builder(),Boolean.FALSE);
    }
    
    public static <T> Builder<T> builder(Boolean useSSL) {
        return builder(WebClient.builder(),useSSL);
    }
        
    public static <T> Builder<T> builder(WebClient.Builder webClientBuilder,Boolean useSSL) {
        return new Builder<>(webClientBuilder,useSSL);
    }

    public static <T> Builder<T> builder(WebClient.Builder webClientBuilder,
                                         WebClientFeignCustomizer webClientCustomizer) {
        return new Builder<>(webClientBuilder, webClientCustomizer);
    }

    public static class Builder<T> extends ReactiveFeign.Builder<T> {

        protected CustomizableWebClientBuilder webClientBuilder;

        protected Builder(WebClient.Builder webClientBuilder,Boolean useSSL) {
            this.webClientBuilder = new CustomizableWebClientBuilder(webClientBuilder);
            this.webClientBuilder.clientConnector(
                    buildNettyClientHttpConnector(WebReactiveOptions.DEFAULT_OPTIONS,useSSL));
            updateClientFactory();
        }

        protected Builder(WebClient.Builder webClientBuilder, WebClientFeignCustomizer webClientCustomizer) {
            this.webClientBuilder = new CustomizableWebClientBuilder(webClientBuilder);
            this.webClientBuilder.clientConnector(
                    buildNettyClientHttpConnector(WebReactiveOptions.DEFAULT_OPTIONS));
            webClientCustomizer.accept(this.webClientBuilder);
            updateClientFactory();
        }

        @Override
        public Builder<T> options(ReactiveOptions options) {
            webClientBuilder.clientConnector(
                    buildNettyClientHttpConnector((WebReactiveOptions)options));
            updateClientFactory();
            return this;
        }

        protected void updateClientFactory(){
            clientFactory(methodMetadata -> webClient(
                    methodMetadata, webClientBuilder.build(), errorMapper()));
        }

        public static BiFunction<ReactiveHttpRequest, Throwable, Throwable> errorMapper(){
            return (request, throwable) -> {
                if(throwable instanceof WebClientRequestException
                   && throwable.getCause() instanceof io.netty.handler.timeout.ReadTimeoutException){
                    return new ReadTimeoutException(throwable, request);
                }
                return null;
            };
        }
    }



}
