package com.evgateway.server.controller.advice;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@SuppressWarnings("serial")
@JsonPropertyOrder({ "message", "key" })
public class ServerException extends Exception {

	@Override
	public synchronized Throwable fillInStackTrace() {
		return this;
	}

	private String key;

	public ServerException(String message) {
		super(message);
	}

	public ServerException(String message, String key) {
		super(message);
		this.key = key;
	}

	public ServerException(String message, Throwable cause) {
		super(message);
	}

	public ServerException(String message, String key, Throwable cause) {
		super(message);
		this.key = key;
	}

	public ServerException(Throwable cause) {
		super("");
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
