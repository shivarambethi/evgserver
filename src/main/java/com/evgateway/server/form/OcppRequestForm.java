package com.evgateway.server.form;

import java.util.Arrays;
import java.util.Date;

public class OcppRequestForm {

	private long stationId;
	private long connectorId;
	private long orgId;
	private String version;
	private String type;

	private String reqType;
	private String idTag;
	private String recurrencyKind;

	private long userId;
	private String key;

	private String value;
	private String retries;

	private String retryInterval;
	private String location;

	private String retrieveDate;
	private Date startTime;
	private Date stopDate;
	private String startDate;
	private String endDate;
	private String validFrom;
	private String validTo;
	private long startPeriod;
	private Date startSchedule;
	private Date retriesDate;

	private String listVersion;
	private String updateType;
	private String chargingProfileInfo;
	private String requestedMessage;
	private String vendorId;

	private String data;
	private long reservationId;

	private String message;
	private String csChargingProfiles;
	private String chargingProfileKind;

	private String chargingProfilePurpose;
	private String stackLevel;

	private String chargingProfileId;
	private long portId;

	private String setting;
	private long rfidPhno;

	private long transactionId;
	private long messageKey;
	private long numberPhases;

	private long retriesIntervals;

	private String fileName;
	private String messageId;

	private String chargingSchedule;
	private String chargingRateUnit;
	private long duration;

	private long limit;
	private long phNo;
	private String[] localAuthorizationLists;

	public String getChargingRateUnit() {
		return chargingRateUnit;
	}

	public void setChargingRateUnit(String chargingRateUnit) {
		this.chargingRateUnit = chargingRateUnit;
	}

	public String getRecurrencyKind() {
		return recurrencyKind;
	}

	public void setRecurrencyKind(String recurrencyKind) {
		this.recurrencyKind = recurrencyKind;
	}

	public String[] getLocalAuthorizationLists() {
		return localAuthorizationLists;
	}

	public void setLocalAuthorizationLists(String[] localAuthorizationLists) {
		this.localAuthorizationLists = localAuthorizationLists;
	}

	public long getProfileId() {
		return profileId;
	}

	public void setProfileId(long profileId) {
		this.profileId = profileId;
	}

	private long profileId;

	public String getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}

	public String getValidTo() {
		return validTo;
	}

	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}

	public String getChargingSchedule() {
		return chargingSchedule;
	}

	public void setChargingSchedule(String chargingSchedule) {
		this.chargingSchedule = chargingSchedule;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getStartPeriod() {
		return startPeriod;
	}

	public void setStartPeriod(long startPeriod) {
		this.startPeriod = startPeriod;
	}

	public long getNumberPhases() {
		return numberPhases;
	}

	public void setNumberPhases(long numberPhases) {
		this.numberPhases = numberPhases;
	}

	public long getLimit() {
		return limit;
	}

	public void setLimit(long limit) {
		this.limit = limit;
	}

	public long getPhNo() {
		return phNo;
	}

	public void setPhNo(long phNo) {
		this.phNo = phNo;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public Date getRetriesDate() {
		return retriesDate;
	}

	public void setRetriesDate(Date retriesDate) {
		this.retriesDate = retriesDate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getRetriesIntervals() {
		return retriesIntervals;
	}

	public void setRetriesIntervals(long retriesIntervals) {
		this.retriesIntervals = retriesIntervals;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public long getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(long messageKey) {
		this.messageKey = messageKey;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public String getChargingProfilePurpose() {
		return chargingProfilePurpose;
	}

	public void setChargingProfilePurpose(String chargingProfilePurpose) {
		this.chargingProfilePurpose = chargingProfilePurpose;
	}

	public String getStackLevel() {
		return stackLevel;
	}

	public void setStackLevel(String stackLevel) {
		this.stackLevel = stackLevel;
	}

	public String getChargingProfileId() {
		return chargingProfileId;
	}

	public void setChargingProfileId(String chargingProfileId) {
		this.chargingProfileId = chargingProfileId;
	}

	public String getCsChargingProfiles() {
		return csChargingProfiles;
	}

	public void setCsChargingProfiles(String csChargingProfiles) {
		this.csChargingProfiles = csChargingProfiles;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	// private Date RetrieveDate;

	public String getRetrieveDate() {
		return retrieveDate;
	}

	public void setRetrieveDate(String retrieveDate) {
		this.retrieveDate = retrieveDate;
	}

	public String getRetries() {
		return retries;
	}

	public void setRetries(String retries) {
		this.retries = retries;
	}

	public String getReqType() {
		return reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getIdTag() {
		return idTag;
	}

	public void setIdTag(String idTag) {
		this.idTag = idTag;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRetryInterval() {
		return retryInterval;
	}

	public void setRetryInterval(String retryInterval) {
		this.retryInterval = retryInterval;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStopDate() {
		return stopDate;
	}

	public void setStopDate(Date stopDate) {
		this.stopDate = stopDate;
	}

	public String getListVersion() {
		return listVersion;
	}

	public void setListVersion(String listVersion) {
		this.listVersion = listVersion;
	}

	public String getUpdateType() {
		return updateType;
	}

	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}

	public String getRequestedMessage() {
		return requestedMessage;
	}

	public void setRequestedMessage(String requestedMessage) {
		this.requestedMessage = requestedMessage;
	}

	public long getReservationId() {
		return reservationId;
	}

	public void setReservationId(long reservationId) {
		this.reservationId = reservationId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getPortId() {
		return portId;
	}

	public void setPortId(long portId) {
		this.portId = portId;
	}

	public String getSetting() {
		return setting;
	}

	public void setSetting(String setting) {
		this.setting = setting;
	}

	public long getRfidPhno() {
		return rfidPhno;
	}

	public void setRfidPhno(long rfidPhno) {
		this.rfidPhno = rfidPhno;
	}

	public long getConnectorId() {
		return connectorId;
	}

	public void setConnectorId(long connectorId) {
		this.connectorId = connectorId;
	}

	public long getStationId() {
		return stationId;
	}

	public void setStationId(long stationId) {
		this.stationId = stationId;
	}

	public String getChargingProfileInfo() {
		return chargingProfileInfo;
	}

	public void setChargingProfileInfo(String chargingProfileInfo) {
		this.chargingProfileInfo = chargingProfileInfo;
	}

	public Date getStartSchedule() {
		return startSchedule;
	}

	public void setStartSchedule(Date startSchedule) {
		this.startSchedule = startSchedule;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getChargingProfileKind() {
		return chargingProfileKind;
	}

	public void setChargingProfileKind(String chargingProfileKind) {
		this.chargingProfileKind = chargingProfileKind;
	}

	@Override
	public String toString() {
		return "OcppRequestForm [stationId=" + stationId + ", connectorId=" + connectorId + ", orgId=" + orgId
				+ ", version=" + version + ", type=" + type + ", reqType=" + reqType + ", idTag=" + idTag
				+ ", recurrencyKind=" + recurrencyKind + ", userId=" + userId + ", key=" + key + ", value=" + value
				+ ", retries=" + retries + ", retryInterval=" + retryInterval + ", location=" + location
				+ ", retrieveDate=" + retrieveDate + ", startTime=" + startTime + ", stopDate=" + stopDate
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", validFrom=" + validFrom + ", validTo="
				+ validTo + ", startPeriod=" + startPeriod + ", startSchedule=" + startSchedule + ", retriesDate="
				+ retriesDate + ", listVersion=" + listVersion + ", updateType=" + updateType + ", chargingProfileInfo="
				+ chargingProfileInfo + ", requestedMessage=" + requestedMessage + ", vendorId=" + vendorId + ", data="
				+ data + ", reservationId=" + reservationId + ", message=" + message + ", csChargingProfiles="
				+ csChargingProfiles + ", chargingProfileKind=" + chargingProfileKind + ", chargingProfilePurpose="
				+ chargingProfilePurpose + ", stackLevel=" + stackLevel + ", chargingProfileId=" + chargingProfileId
				+ ", portId=" + portId + ", setting=" + setting + ", rfidPhno=" + rfidPhno + ", transactionId="
				+ transactionId + ", messageKey=" + messageKey + ", numberPhases=" + numberPhases
				+ ", retriesIntervals=" + retriesIntervals + ", fileName=" + fileName + ", messageId=" + messageId
				+ ", chargingSchedule=" + chargingSchedule + ", chargingRateUnit=" + chargingRateUnit + ", duration="
				+ duration + ", limit=" + limit + ", phNo=" + phNo + ", localAuthorizationLists="
				+ Arrays.toString(localAuthorizationLists) + ", profileId=" + profileId + "]";
	}

}
