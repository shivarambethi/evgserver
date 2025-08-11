package com.evgateway.server.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "preferredNotification")
public class PreferredNotification extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private boolean rfidStatus;

	private boolean sessionEnds;

	private boolean stationAvailability;

	private long userId;

	private long orgId;
	
	private boolean notification;
	
	private boolean email;
	
	private boolean sms;
	
	public boolean isRfidStatus() {
		return rfidStatus;
	}

	public void setRfidStatus(boolean rfidStatus) {
		this.rfidStatus = rfidStatus;
	}

	public boolean isSessionEnds() {
		return sessionEnds;
	}

	public void setSessionEnds(boolean sessionEnds) {
		this.sessionEnds = sessionEnds;
	}

	public boolean isStationAvailability() {
		return stationAvailability;
	}

	public void setStationAvailability(boolean stationAvailability) {
		this.stationAvailability = stationAvailability;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}
	
	@Column(name = "notification")
	public boolean isNotification() {
		return notification;
	}

	public void setNotification(boolean notification) {
		this.notification = notification;
	}

	@Column(name = "email")
	public boolean isEmail() {
		return email;
	}

	public void setEmail(boolean email) {
		this.email = email;
	}

	@Column(name = "sms")
	public boolean isSms() {
		return sms;
	}

	public void setSms(boolean sms) {
		this.sms = sms;
	}

	@Override
	public String toString() {
		return "PreferredNotification [rfidStatus=" + rfidStatus + ", sessionEnds=" + sessionEnds
				+ ", stationAvailability=" + stationAvailability + ", userId=" + userId + ", orgId=" + orgId
				+ ", notification=" + notification + ", email=" + email + ", sms=" + sms + "]";
	}

	
}
