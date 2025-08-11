package com.evgateway.server.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "site_timing_days")
public class SiteTimingDays extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long weekdaysId;
	private long dayId;

	@JsonIgnore
	private SiteTiming siteTiming;

	public long getWeekdaysId() {
		return weekdaysId;
	}

	public void setWeekdaysId(long weekdaysId) {
		this.weekdaysId = weekdaysId;
	}

	@JoinColumn(name = "siteTimingId")
	@ManyToOne(fetch = FetchType.LAZY)
	public SiteTiming getSiteTiming() {
		return siteTiming;
	}

	public void setSiteTiming(SiteTiming siteTiming) {
		this.siteTiming = siteTiming;
	}

	

	@Column(name="dayId",columnDefinition = "double precision default 0 not null")
	public long getDayId() {
		return dayId;
	}

	public void setDayId(long dayId) {
		this.dayId = dayId;
	}

}
