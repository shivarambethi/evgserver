package com.evgateway.server.form;

public class SiteTimingform {

	private String days;
	private String openTeime;
	private String closeTime;
	private long dayId;
	
	public long getDayId() {
		return dayId;
	}
	public void setDayId(long dayId) {
		this.dayId = dayId;
	}
	public String getDays() {
		return days;
	}
	public void setDays(String days) {
		this.days = days;
	}
	public String getCloseTime() {
		return closeTime;
	}
	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}
	public String getOpenTeime() {
		return openTeime;
	}
	public void setOpenTeime(String openTeime) {
		this.openTeime = openTeime;
	}
	
}
