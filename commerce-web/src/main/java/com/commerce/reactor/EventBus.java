package com.commerce.reactor;

import java.util.List;

import com.commerce.reactor.listeners.CustomEventListener;
import com.commerce.reactor.processors.EventProcessor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

/**
 * 建立一個可從外部push資料到Flux。
 * 
 * @author Tony.su
 */
public class EventBus<T> {

	private EventProcessor<T> eventProcessor;

	private Flux<T> flux;

	public EventBus(EventProcessor<T> eventProcessor) {
		super();
		this.eventProcessor = eventProcessor;
		this.flux = createBridge().subscribeOn(Schedulers.newParallel("event-scheduler", 10));
	}

	private Flux<T> createBridge() {
		return Flux.create(sink -> eventProcessor.register(new CustomEventListener<>(sink)),
				FluxSink.OverflowStrategy.ERROR);
	}

	public void dispathData(List<T> chunks) {
		eventProcessor.dispathData(chunks);
	}

	public void dispathData(T chunk) {
		eventProcessor.dispathData(chunk);
	}
	
	public List<T> dispathDataWith(List<T> chunks) {
		eventProcessor.dispathData(chunks);
		return chunks;
	}

	public T dispathDataWith(T chunk) {
		eventProcessor.dispathData(chunk);
		return chunk;
	}

	public Flux<T> getFlux() {
		return flux;
	}
}
