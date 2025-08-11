package com.evgateway.server.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "users_in_sites")
@JsonIgnoreProperties(ignoreUnknown = true, value = { "hibernateLazyInitializer", "handler" })
public class UserInSite implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	@Id
	@Column(nullable = false)
	private long siteId;
	
	private long userId;
	
	public UserInSite(long userId, long siteId) {
		super();
		this.userId = userId;
		this.siteId = siteId;
	}
	
	

	public UserInSite() {
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
		return "UserInSite [userId=" + userId + ", siteId=" + siteId + "]";
	}

}
