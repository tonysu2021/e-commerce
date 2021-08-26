package com.commerce.client.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.commerce.client.util.CommonUtils;
import com.commerce.reactor.subscribers.EventSubscriber;
import com.commerce.web.client.WebClientTemplate;
import com.commerce.web.response.LiveComResponse;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Service("liveComService")
public class LiveComService {

	private static Logger logger = LoggerFactory.getLogger(LiveComService.class);

	private static final String TEMPLATE = "/${liveId}/live_comments?access_token=${accessToken}&comment_rate=one_hundred_per_second";

	private Map<String, EventSubscriber<LiveComResponse>> registry = new ConcurrentHashMap<>(16);

	private Scheduler liveSchedulers = Schedulers.newParallel("live-com-scheduler", 20);

	@Autowired
	@Qualifier("fbWebClient")
	private WebClientTemplate webClientTemplate;

	public void createLiveCom(String liveId, String accessToken) {
		if (registry.containsKey(liveId)) {
			logger.debug("TThe LiveCom {} is exist ", liveId);
			return;
		}
		Map<String, Object> values = new HashMap<>(16);
		values.put("liveId", liveId);
		values.put("accessToken", accessToken);

		Flux<LiveComResponse> facebookFlux = webClientTemplate
				.getWithFlux(CommonUtils.format(TEMPLATE, values), LiveComResponse.class).publishOn(liveSchedulers).log();
		EventSubscriber<LiveComResponse> subscriber = facebookFlux.subscribeWith(new EventSubscriber<>(10, 10));
		registry.put(liveId, subscriber);
	}

	public boolean closeLiveCom(String liveId) {
		logger.debug("close LiveCom {}", liveId);
		registry.remove(liveId).cancel();
		return true;
	}
}
