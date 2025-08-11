package com.evgateway.server.utils;

import java.util.Date;

import com.evgateway.server.messages.PagedResult;

public class ResponsePaged<T> {

	public PagedResult<T> data;

	public int status;

	public String message;

	public Date timestamp;

	public ResponsePaged() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ResponsePaged(PagedResult<T> data, int status, String message, Date timestamp) {
		super();
		this.data = data;
		this.status = status;
		this.message = message;
		this.timestamp = timestamp;
	}

	public PagedResult<T> getData() {
		return data;
	}

	public void setData(PagedResult<T> data) {
		this.data = data;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

}
