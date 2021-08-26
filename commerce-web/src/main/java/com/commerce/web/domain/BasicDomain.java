package com.commerce.web.domain;

import com.commerce.reactor.EventType;

public class BasicDomain {
		
	private EventType eventType;
	
	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	
}
