package com.evgateway.server.ocpp;

import java.util.List;

public class RemoteStop {

	private long stationId;
	private List<Long> stationIdList;
	private long connectorId;
	private String requestType;
	private String clientId="Portal";
	private boolean powerSharing;

	public boolean isPowerSharing() {
		return powerSharing;
	}

	public void setPowerSharing(boolean powerSharing) {
		this.powerSharing = powerSharing;
	}

	public RemoteStop() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public long getStationId() {
		return stationId;
	}

	public void setStationId(long stationId) {
		this.stationId = stationId;
	}

	public long getConnectorId() {
		return connectorId;
	}

	public void setConnectorId(long connectorId) {
		this.connectorId = connectorId;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public List<Long> getStationIdList() {
		return stationIdList;
	}

	public void setStationIdList(List<Long> stationIdList) {
		this.stationIdList = stationIdList;
	}

	public RemoteStop(long stationId, long connectorId) {
		super();
		this.stationId = stationId;
		this.connectorId = connectorId;
		this.requestType = "RemoteStop";
		this.clientId = "Portal";
	}

	public RemoteStop(List<Long> stationIdList, long connectorId) {
		super();
		this.stationIdList = stationIdList;
		this.connectorId = connectorId;
		this.requestType = "RemoteStop";
		this.clientId = "Portal";
	}

	@Override
	public String toString() {
		return "RemoteStop [stationId=" + stationId + ", stationIdList=" + stationIdList + ", connectorId="
				+ connectorId + ", requestType=" + requestType + ", clientId=" + clientId + ", powerSharing="
				+ powerSharing + "]";
	}

}
