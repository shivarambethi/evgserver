package com.evgateway.server.utils;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ErrorResponse {

	private int status_code;

	private String status_message;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private Date timestamp = new Date();

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getStatus_message() {
		return status_message;
	}

	public ErrorResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ErrorResponse(HttpStatus httpStatus, String status_message) {
		super();
		this.setStatus_code(httpStatus.value());
		this.status_message = status_message;
	}

	public ErrorResponse(Date timestamp, HttpStatus httpStatus, String status_message) {
		super();
		this.timestamp = timestamp;
		this.setStatus_code(httpStatus.value());
		this.status_message = status_message;
	}

	public void setStatus_message(String status_message) {
		this.status_message = status_message;
	}

	public int getStatus_code() {
		return status_code;
	}

	public void setStatus_code(int status_code) {
		this.status_code = status_code;
	}
}
