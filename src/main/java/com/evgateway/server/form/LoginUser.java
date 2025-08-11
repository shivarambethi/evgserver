package com.evgateway.server.form;

import java.util.List;
import java.util.Map;

public class LoginUser {
	private String username;
	private String email;
	private String password;
	private List deviceInfo;
	private long orgId;
	private String otp;

	// For Vesrion2.0
	private Map<String, String> UserInfo;
	private Map<String, String> DeviceInfo;
	private Map<String, String> address;
	private String orgName;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/*
	 * public List getDeviceInfo() { return deviceInfo; } public void
	 * setDeviceInfo(List deviceInfo) { this.deviceInfo = deviceInfo; }
	 */
	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public Map<String, String> getUserInfo() {
		return UserInfo;
	}

	public void setUserInfo(Map<String, String> userInfo) {
		UserInfo = userInfo;
	}

	public Map<String, String> getDeviceInfo() {
		return DeviceInfo;
	}

	public void setDeviceInfo(Map<String, String> deviceInfo) {
		DeviceInfo = deviceInfo;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public Map<String, String> getAddress() {
		return address;
	}

	public void setAddress(Map<String, String> address) {
		this.address = address;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	@Override
	public String toString() {
		return "LoginUser [username=" + username + ", email=" + email + ", deviceInfo=" + deviceInfo + ", orgId="
				+ orgId + ", otp=" + otp + ", DeviceInfo=" + DeviceInfo + ", address=" + address + ", orgName="
				+ orgName + "]";
	}

}
