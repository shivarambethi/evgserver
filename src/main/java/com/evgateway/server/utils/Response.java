package com.evgateway.server.utils;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Response<T> {

	public T data;

	public int status_code;

	public String status_message;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	public Date timestamp;

	public Response() {

	}

	public Response(T data, int status_code, String status_message, Date timestamp) {
		super();
		this.data = data;
		this.status_code = status_code;
		this.status_message = status_message;
		this.timestamp = timestamp;
	}

	public Response(Date timestamp, int status_code, String status_message) {
		this.status_code = status_code;
		this.status_message = status_message;
		this.timestamp = timestamp;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public int getStatus_code() {
		return status_code;
	}

	public void setStatus_code(int status_code) {
		this.status_code = status_code;
	}

	public String getStatus_message() {
		return status_message;
	}

	public void setStatus_message(String status_message) {
		this.status_message = status_message;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "Response [data=" + data + ", status_code=" + status_code + ", status_message=" + status_message
				+ ", timestamp=" + timestamp + "]";
	}

}