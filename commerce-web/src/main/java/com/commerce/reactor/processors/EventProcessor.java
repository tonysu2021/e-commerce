package com.commerce.reactor.processors;

import java.util.List;

import com.commerce.reactor.listeners.EventListener;

/**
 * 自定義Processor主要橋接Source、Sink介面，並且提供外部操作Source方式。
 * 
 * @author Tony.su
 * @param <T>
 * 
 */
public interface EventProcessor<T> {

	void register(EventListener<T> eventListener);

	void unregister(String uuidStr);

	void dispathData(List<T> chunks);

	void dispathData(T chunk);

	void shutdown();
}
