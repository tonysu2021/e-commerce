package com.commerce.reactor.processors;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.commerce.reactor.listeners.EventListener;

public class CustomEventProcessor<T> implements EventProcessor<T> {

	private static Logger logger = LoggerFactory.getLogger(CustomEventProcessor.class);

	private Map<String, EventListener<T>> registry;

	private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

	public CustomEventProcessor() {
		// 本身具備多線程安全以及lock-free
		this.registry = new ConcurrentHashMap<>(16);
		// 檢查註冊EventListener是否健康，每隔30秒清除不健康的EventListener。
		this.executor.scheduleAtFixedRate(this::checkHealth, 1, 30, TimeUnit.SECONDS);
	}

	@Override
	public void register(EventListener<T> eventListener) {
		String uuidStr = eventListener.getUuid().toString();
		logger.debug("Register EventListener {}", uuidStr);
		registry.put(uuidStr, eventListener);

	}

	@Override
	public void unregister(String uuidStr) {
		logger.debug("Unregister EventListener {}", uuidStr);
		registry.remove(uuidStr);
	}

	@Override
	public void dispathData(List<T> chunks) {
		registry.entrySet().parallelStream().filter(element -> !element.getValue().isCancelled())
				.forEach(element -> element.getValue().onDataChunk(chunks));
	}

	@Override
	public void dispathData(T chunk) {
		registry.entrySet().parallelStream().filter(element -> !element.getValue().isCancelled())
				.forEach(element -> element.getValue().onDataChunk(chunk));
	}

	@Override
	public void shutdown() {
		registry.entrySet().parallelStream().forEach(element -> element.getValue().processComplete());
		registry = new ConcurrentHashMap<>(16);
	}

	private void checkHealth() {
		registry.entrySet().parallelStream().filter(element -> element.getValue().isCancelled())
				.forEach(element -> unregister(element.getKey()));
	}

}
