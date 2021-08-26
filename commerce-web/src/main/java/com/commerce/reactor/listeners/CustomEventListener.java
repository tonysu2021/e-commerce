package com.commerce.reactor.listeners;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.FluxSink;

public class CustomEventListener<T> implements EventListener<T> {

	private static Logger logger = LoggerFactory.getLogger(CustomEventListener.class);

	private UUID uuid;

	private FluxSink<T> sink;

	public CustomEventListener(FluxSink<T> sink) {
		this.uuid = UUID.randomUUID();
		this.sink = sink;
	}

	@Override
	public UUID getUuid() {
		return uuid;
	}

	@Override
	public boolean isCancelled() {
		return sink.isCancelled();
	}
	
	@Override
	public void onDataChunk(List<T> chunks) {
		if (chunks == null || chunks.isEmpty()) {
			return;
		}
		for (T s : chunks) {
			// 終止條件: 如果訊息消費者 Sink關閉連線，則停止
			if (sink.isCancelled()) {
				logger.debug("sink is cancelled");
				sink.complete();
			} else {
				sink.next(s);
			}
		}
	}

	@Override
	public void onDataChunk(T chunk) {
		if (chunk == null) {
			return;
		}
		if (sink.isCancelled()) {
			sink.complete();
		} else {
			sink.next(chunk);
		}

	}

	@Override
	public void processComplete() {
		sink.complete();
	}

	@Override
	public void processError(Throwable e) {
		sink.error(e);
	}



}
