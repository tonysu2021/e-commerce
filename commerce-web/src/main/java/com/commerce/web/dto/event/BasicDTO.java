package com.commerce.web.dto.event;

import com.commerce.reactor.EventType;

public class BasicDTO {
		
	private EventType eventType;
	
	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	
}
