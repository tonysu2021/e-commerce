package com.commerce.reactor.subscribers;

import org.reactivestreams.Subscription;

import reactor.core.publisher.BaseSubscriber;

public class EventSubscriber<T> extends BaseSubscriber<T> {

	private int consumed;

	/** [Back Pressure(背壓)] */
	private final int initialFetchCount;
	private final int onNextFetchCount;

	public EventSubscriber(int initialFetchCount, int onNextFetchCount) {
		super();
		this.initialFetchCount = initialFetchCount;
		this.onNextFetchCount = onNextFetchCount;
	}

	@Override
	public void hookOnSubscribe(Subscription subscription) {
		request(initialFetchCount);
	}

	@Override
	public void hookOnNext(T value) {
		consumed++;
		if (consumed == onNextFetchCount) {
			consumed = 0;
			request(onNextFetchCount);
		}

	}
}
