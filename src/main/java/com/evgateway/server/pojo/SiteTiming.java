package com.evgateway.server.pojo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "site_timing")
public class SiteTiming extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<SiteTimingDays> days = new ArrayList<SiteTimingDays>(0);

	private Set<Long> day = new HashSet<Long>(0);

	@Transient
	public Set<Long> getDay() {
		return day;
	}

	public void setDay(Set<Long> day) {

		this.day = day;
	}

	private String openingTime;
	private String closingTime;
	private long siteId;
	private String timeZone;
	private String userTimeZone;

	public String getOpeningTime() {
		return openingTime;
	}

	public void setOpeningTime(String openingTime) {
		this.openingTime = openingTime;
	}

	public String getClosingTime() {
		return closingTime;
	}

	public void setClosingTime(String closingTime) {
		this.closingTime = closingTime;
	}

	public long getSiteId() {
		return siteId;
	}

	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "siteTiming")
	public List<SiteTimingDays> getDays() {
		return days;
	}

	public void setDays(List<SiteTimingDays> days) {
		this.days = days;
	}

	

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getUserTimeZone() {
		return userTimeZone;
	}

	public void setUserTimeZone(String userTimeZone) {
		this.userTimeZone = userTimeZone;
	}

}
