package com.commerce.web.exception;

import java.util.Collections;
import java.util.Map;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 客製化Exception參考ResponseStatusException
 * 
 */
@SuppressWarnings("serial")
public class ResponseStatusCodeException extends NestedRuntimeException {

	private final int status;

	@Nullable
	private final String reason;

	private final int code;
	
	public ResponseStatusCodeException(HttpStatus status) {
		this(status, ExceptionCode.NONE, null, null);
	}

	public ResponseStatusCodeException(HttpStatus status, ExceptionCode code) {
		this(status, code, null, null);
	}

	public ResponseStatusCodeException(HttpStatus status, ExceptionCode code, @Nullable String reason) {
		this(status, code, reason, null);
	}

	public ResponseStatusCodeException(HttpStatus status, ExceptionCode code, @Nullable String reason, @Nullable Throwable cause) {
		super(null, cause);
		Assert.notNull(status, "HttpStatus is required");
		Assert.notNull(code, "ExceptionCode is required");
		this.status = status.value();
		this.code = code.getCode();
		this.reason = reason;
	}

	public ResponseStatusCodeException(int rawStatusCode, int exceptionCode, @Nullable String reason, @Nullable Throwable cause) {
		super(null, cause);
		this.status = rawStatusCode;
		this.code = exceptionCode;
		this.reason = reason;
	}

	public HttpStatus getStatus() {
		return HttpStatus.valueOf(this.status);
	}

	public int getRawStatusCode() {
		return this.status;
	}

	@Deprecated
	public Map<String, String> getHeaders() {
		return Collections.emptyMap();
	}

	public HttpHeaders getResponseHeaders() {
		Map<String, String> headers = getHeaders();
		if (headers.isEmpty()) {
			return HttpHeaders.EMPTY;
		}
		HttpHeaders result = new HttpHeaders();
		getHeaders().forEach(result::add);
		return result;
	}
	
	public int getCode() {
		return code;
	}

	@Nullable
	public String getReason() {
		return this.reason;
	}

	@Override
	public String getMessage() {
		HttpStatus status = HttpStatus.resolve(this.status);
		String msg = (status != null ? status : this.status) + (this.reason != null ? " \"" + this.reason + "\"" : "");
		return NestedExceptionUtils.buildMessage(msg, getCause());
	}

}