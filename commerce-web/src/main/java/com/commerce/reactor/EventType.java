package com.commerce.reactor;

public enum EventType {
	DATA_CREATE("CREATE"), DATA_UPATE("UPATE"), DATA_DELETE("DELETE"),PING("ping");

	EventType(final String desc) {
		this.desc = desc;
	}

	private String desc;

	public String getDesc() {
		return desc;
	}
}
