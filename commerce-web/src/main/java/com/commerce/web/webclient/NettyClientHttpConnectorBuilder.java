package com.commerce.web.webclient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLException;

import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;
import reactor.netty.transport.ProxyProvider;

class NettyClientHttpConnectorBuilder {
	
	private NettyClientHttpConnectorBuilder() {}

	
	public static ClientHttpConnector buildNettyClientHttpConnector(WebReactiveOptions webOptions) {
		return buildNettyClientHttpConnector(webOptions,Boolean.FALSE);
	}
	
    @SuppressWarnings("deprecation")
	public static ClientHttpConnector buildNettyClientHttpConnector(WebReactiveOptions webOptions,Boolean useSSL) {
        TcpClient tcpClient = TcpClient.create();
        if (webOptions.getConnectTimeoutMillis() != null) {
            tcpClient = tcpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,
                    webOptions.getConnectTimeoutMillis().intValue());
        }
        tcpClient = tcpClient.doOnConnected(connection -> {
            if(webOptions.getReadTimeoutMillis() != null){
                connection.addHandlerLast(new ReadTimeoutHandler(
                        webOptions.getReadTimeoutMillis(), TimeUnit.MILLISECONDS));
            }
            if(webOptions.getWriteTimeoutMillis() != null){
                connection.addHandlerLast(new WriteTimeoutHandler(
                        webOptions.getWriteTimeoutMillis(), TimeUnit.MILLISECONDS));
            }
        });

        WebReactiveOptions.WebProxySettings proxySettings = (WebReactiveOptions.WebProxySettings)webOptions.getProxySettings();
        if(proxySettings != null){
            tcpClient = tcpClient.proxy(typeSpec -> {
                ProxyProvider.Builder proxyBuilder = typeSpec.type(ProxyProvider.Proxy.HTTP)
                        .host(proxySettings.getHost())
                        .port(proxySettings.getPort())
                        .username(proxySettings.getUsername())
                        .password(password -> proxySettings.getPassword());
                if(proxySettings.getTimeout() != null){
                    proxyBuilder.connectTimeoutMillis(proxySettings.getTimeout());
                }
            });
        }

        HttpClient httpClient = HttpClient.from(tcpClient);
        
        /**自訂義使用SSL*/
        if (Boolean.TRUE == useSSL) {
        	try {
				SslContext sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE)
						.build();
				httpClient = httpClient.secure(t -> t.sslContext(sslContext));
        	} catch (SSLException e) {
    			// Donothing
    		}
        }

        if(webOptions.getResponseTimeoutMillis() != null){
            httpClient = httpClient.responseTimeout(Duration.ofMillis(webOptions.getResponseTimeoutMillis()));
        }

        if (webOptions.isTryUseCompression() != null) {
            httpClient = httpClient.compress(true);
        }
        if(webOptions.isFollowRedirects() != null){
            httpClient = httpClient.followRedirect(webOptions.isFollowRedirects());
        }
        return new ReactorClientHttpConnector(httpClient);
    }
}
