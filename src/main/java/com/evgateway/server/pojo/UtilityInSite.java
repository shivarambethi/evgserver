package com.evgateway.server.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "utility_in_sites")
@JsonIgnoreProperties(ignoreUnknown = true, value = { "hibernateLazyInitializer", "handler" })
public class UtilityInSite extends BaseEntity {
	private static final long serialVersionUID = 1L;

	private long siteId;
	
	private long userId;
	
	public UtilityInSite() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getSiteId() {
		return siteId;
	}

	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}

	@Override
	public String toString() {
		return "UtilityInSite [userId=" + userId + ", siteId=" + siteId + "]";
	}

}
