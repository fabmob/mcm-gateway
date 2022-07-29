package com.gateway.commonapi.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class GlobalExceptionHandler extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GlobalExceptionHandler(String message) {
		super(message);
	}
}