package com.evgateway.server.ocpp;

import java.util.List;

public class RemoteStart {

	private long stationId;
	private List<Long> stationIdList;
	private long connectorId;
	private String requestType;
	private String IdTag;
	private long orgId;
	private String clientId="Portal";
	
	private boolean powerSharing;

	public boolean isPowerSharing() {
		return powerSharing;
	}

	public void setPowerSharing(boolean powerSharing) {
		this.powerSharing = powerSharing;
	}

	public RemoteStart() {
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

	public String getIdTag() {
		return IdTag;
	}

	public void setIdTag(String idTag) {
		IdTag = idTag;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public List<Long> getStationIdList() {
		return stationIdList;
	}

	public void setStationIdList(List<Long> stationIdList) {
		this.stationIdList = stationIdList;
	}

	
	public RemoteStart(long stationId, long connectorId, String idTag, long orgId) {
		super();
		this.stationId = stationId;
		this.connectorId = connectorId;
		this.requestType = "RemoteStart";
		this.IdTag = idTag;
		this.orgId = orgId;
		this.clientId = "Portal";
	}

	public RemoteStart(List<Long> stationIdList, long connectorId, String idTag, long orgId) {
		super();
		this.stationIdList = stationIdList;
		this.connectorId = connectorId;
		this.requestType = "RemoteStart";
		IdTag = idTag;
		this.orgId = orgId;
		this.clientId = "Portal";
	}

	@Override
	public String toString() {
		return "RemoteStart [stationId=" + stationId + ", stationIdList=" + stationIdList + ", connectorId="
				+ connectorId + ", requestType=" + requestType + ", IdTag=" + IdTag + ", orgId=" + orgId + ", clientId="
				+ clientId + ", powerSharing=" + powerSharing + "]";
	}
	

}
