package com.evgateway.server.model;

public class StationDataForChargingSession {

	private long stationId;
	private String stationAddress;
	private String referNo;
	private String stationName;
	private String stationType;
	private String capacity;
	private String currencyType;
	private String currencySymbol;
	
	public long getStationId() {
		return stationId;
	}

	public void setStationId(long stationId) {
		this.stationId = stationId;
	}

	public String getStationAddress() {
		return stationAddress;
	}

	public void setStationAddress(String stationAddress) {
		this.stationAddress = stationAddress;
	}

	public String getReferNo() {
		return referNo;
	}

	public void setReferNo(String referNo) {
		this.referNo = referNo;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getStationType() {
		return stationType;
	}

	public void setStationType(String stationType) {
		this.stationType = stationType;
	}

	public String getCapacity() {
		return capacity;
	}

	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	@Override
	public String toString() {
		return "StationDataForChargingSession [stationId=" + stationId + ", stationAddress=" + stationAddress
				+ ", referNo=" + referNo + ", stationName=" + stationName + ", stationType=" + stationType
				+ ", capacity=" + capacity + ", currencyType=" + currencyType + ", currencySymbol=" + currencySymbol
				+ "]";
	}

}
