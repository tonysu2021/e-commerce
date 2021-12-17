package com.commerce.web.conf;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.commerce.web.dto.response.StatusResponse;
import com.commerce.web.exception.ExceptionCode;
import com.commerce.web.exception.ResponseStatusCodeException;


@Configuration
public class CommonServiceConfiguration {

    @RestControllerAdvice
    @Order(Ordered.HIGHEST_PRECEDENCE)
	private class RestExceptionHandler {
		private final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

		@ExceptionHandler(Exception.class)
		@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
		protected ResponseEntity<Object> genericExceptionHandler(Exception ex) {
			StatusResponse apiStatus = new StatusResponse(HttpStatus.INTERNAL_SERVER_ERROR.value());
			apiStatus.setMessage(ex.getMessage());
			apiStatus.setDebugMessage(ex.getMessage());
			logger.debug("Exception : {}", ex.getMessage());
			return buildResponseEntity(apiStatus);
		}

		@ExceptionHandler(ResponseStatusCodeException.class)
		public ResponseEntity<Object> preconditionException(ResponseStatusCodeException ex) {
			ExceptionCode code = ExceptionCode.valueOfCode(ex.getCode());
			StatusResponse apiStatus = new StatusResponse(ex.getStatus().value());
			apiStatus.setCode(code.getCode());
			apiStatus.setMessage(code.getMsg());
			return buildResponseEntity(apiStatus);
		}

		/**
		 * catch method argument throw exception for hibernate validator
		 * 
		 * @param ex
		 * @return
		 * 
		 */
		@ExceptionHandler(WebExchangeBindException.class)
		protected ResponseEntity<Object> handleWebExchangeBindException(WebExchangeBindException ex) {
			StatusResponse apiStatus = new StatusResponse(HttpStatus.BAD_REQUEST.value());
			apiStatus.setMessage(HttpStatus.BAD_REQUEST.name());
			StringBuilder message = new StringBuilder();
			List<FieldError> fieldErrors = ex.getFieldErrors();
			if (!CollectionUtils.isEmpty(fieldErrors)) {
				for (FieldError fieldError : fieldErrors) {
					String debugMessage = fieldError.getDefaultMessage();
					if (StringUtils.isNotEmpty(debugMessage)) {
						message.append(debugMessage.concat(";"));
					}
				}
			}
			apiStatus.setDebugMessage(message.toString());
			return buildResponseEntity(apiStatus);
		}
		
		/**
		 * catch constraint throw exception for hibernate validator
		 * 
		 * @param ex
		 * @return
		 * 
		 */
		@ExceptionHandler(ConstraintViolationException.class)
		protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
			StatusResponse apiStatus = new StatusResponse(HttpStatus.BAD_REQUEST.value());
			apiStatus.setMessage(HttpStatus.BAD_REQUEST.name());
			StringBuilder message = new StringBuilder();
			Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
			for (ConstraintViolation<?> violation : violations) {
				message.append(violation.getMessage().concat(";"));
			}
			apiStatus.setDebugMessage(message.toString());
			return buildResponseEntity(apiStatus);
		}


		private ResponseEntity<Object> buildResponseEntity(StatusResponse apiStatus) {
			return new ResponseEntity<>(apiStatus, HttpStatus.valueOf(apiStatus.getStatus()));
		}
	}
}

