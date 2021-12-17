package com.commerce.web.dto.response;

import java.io.Serializable;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusResponse implements Serializable {

    private static final long serialVersionUID = 6L;
    @JsonIgnore
    private int status;
    private int code;
    private Instant timestamp;
    private String message;
    private String debugMessage;

    public StatusResponse() {
        this.timestamp = Instant.now();
    }

    public StatusResponse(int status) {
        this();
        this.status = status;
    }

    public StatusResponse(int status, Throwable ex) {
        this();
        this.status = status;
        this.message = ("Unexpected error");
        this.debugMessage = ex.getLocalizedMessage();
    }

    public StatusResponse(int status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

}
