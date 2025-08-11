package com.evgateway.server.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;



@Entity
@Table(name = "appVersion")
public class AppVersion extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String appVersion;

	private String deviceType;

	private Long orgId;

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	@Override
	public String toString() {
		return "AppVersion [appVersion=" + appVersion + ", deviceType=" + deviceType + ", orgId=" + orgId + "]";
	}

}
