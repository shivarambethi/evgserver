package com.evgateway.server.form;

import com.evgateway.server.pojo.Entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties
public class DeviceForm implements Entity {
	private int userId;
	private String appInfo;
	private String deviceName;
	private String deviceVersion;
	private String deviceType;
	private String devicetoken;
	private String legacyKey;
	private String deviceToken;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getAppInfo() {
		return appInfo;
	}

	public void setAppInfo(String appInfo) {
		this.appInfo = appInfo;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceVersion() {
		return deviceVersion;
	}

	public void setDeviceVersion(String deviceVersion) {
		this.deviceVersion = deviceVersion;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDevicetoken() {
		return devicetoken;
	}

	public void setDevicetoken(String devicetoken) {
		this.devicetoken = devicetoken;
	}

	public String getLegacyKey() {
		return legacyKey;
	}

	public void setLegacyKey(String legacyKey) {
		this.legacyKey = legacyKey;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	@Override
	public String toString() {
		return "DeviceForm [userId=" + userId + ", appInfo=" + appInfo + ", deviceName=" + deviceName
				+ ", deviceVersion=" + deviceVersion + ", deviceType=" + deviceType + ", devicetoken=" + devicetoken
				+ ", legacyKey=" + legacyKey + ", deviceToken=" + deviceToken + "]";
	}

}
