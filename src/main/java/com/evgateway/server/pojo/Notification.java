package com.evgateway.server.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "notification")
public class Notification extends BaseEntity {
	private static final long serialVersionUID = 1L;

	private String createdBy;

	@Temporal(TemporalType.DATE)
	@Column(name = "createdDate", length = 10)
	private Date createdDate = new Date();

	private long userId;

	private String title;

	private long stationid;

	private long orgId;

	private String comment;

	@Column(name = "seen", nullable = false)
	@JsonProperty("seen")
	private boolean seen;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getStationid() {
		return stationid;
	}

	public void setStationid(long stationid) {
		this.stationid = stationid;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isSeen() {
		return seen;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	@Override
	public String toString() {
		return "Notification [createdBy=" + createdBy + ", createdDate=" + createdDate + ", userId=" + userId
				+ ", title=" + title + ", stationid=" + stationid + ", orgId=" + orgId + ", comment=" + comment
				+ ", seen=" + seen + "]";
	}

}
