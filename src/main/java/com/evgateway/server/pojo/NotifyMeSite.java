package com.evgateway.server.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "notify_me_site")
public class NotifyMeSite extends BaseEntity{

	private static final long serialVersionUID = 1L;

	private String uuid;
	private long siteId;
	private String deviceType;
	private String deviceToken;
	private long orgId;
	private boolean powerOutage;
	private boolean scheduledMaintenance;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public long getSiteId() {
		return siteId;
	}

	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public boolean isPowerOutage() {
		return powerOutage;
	}

	public void setPowerOutage(boolean powerOutage) {
		this.powerOutage = powerOutage;
	}

	public boolean isScheduledMaintenance() {
		return scheduledMaintenance;
	}

	public void setScheduledMaintenance(boolean scheduledMaintenance) {
		this.scheduledMaintenance = scheduledMaintenance;
	}

	@Override
	public String toString() {
		return "NotifyMeSite [uuid=" + uuid + ", siteId=" + siteId + ", deviceType=" + deviceType + ", deviceToken="
				+ deviceToken + ", orgId=" + orgId + ", powerOutage=" + powerOutage + ", scheduledMaintenance="
				+ scheduledMaintenance + "]";
	}

}
