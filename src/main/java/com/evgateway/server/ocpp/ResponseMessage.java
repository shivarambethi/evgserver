package com.evgateway.server.ocpp;

import java.util.Date;

public class ResponseMessage {
	private String status = "Rejected";
	private String message = "";
	private Date timestamp = new Date();
	private String clientId="Portal";
	private String sessionId;
	private int statusCode;
	private long connectorId;
	private String transactionId;
	private long reservationId;

	public ResponseMessage(String message) {
		this.message = message;
	}

	public ResponseMessage() {
		super();
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public long getConnectorId() {
		return connectorId;
	}

	public void setConnectorId(long connectorId) {
		this.connectorId = connectorId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public long getReservationId() {
		return reservationId;
	}

	public void setReservationId(long reservationId) {
		this.reservationId = reservationId;
	}

	@Override
	public String toString() {
		return "ResponseMessage [status=" + status + ", message=" + message + ", timestamp=" + timestamp + ", clientId="
				+ clientId + ", sessionId=" + sessionId + ", statusCode=" + statusCode + ", connectorId=" + connectorId
				+ ", transactionId=" + transactionId + ", reservationId=" + reservationId + "]";
	}

}
