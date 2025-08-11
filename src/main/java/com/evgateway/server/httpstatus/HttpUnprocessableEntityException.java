package com.evgateway.server.httpstatus;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class HttpUnprocessableEntityException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
