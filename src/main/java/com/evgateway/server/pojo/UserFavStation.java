package com.evgateway.server.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "user_fav_station")
@JsonIgnoreProperties(ignoreUnknown = true, value = { "hibernateLazyInitializer", "handler" })
public class UserFavStation implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Long id;
	
	private long stationId;

	private long userId;

	public UserFavStation() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserFavStation(long stationId, long userId) {
		super();
		this.stationId = stationId;
		this.userId = userId;
	}

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public long getStationId() {
		return stationId;
	}

	public void setStationId(long stationId) {
		this.stationId = stationId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "UserFavStation [stationId=" + stationId + ", userId=" + userId + "]";
	}

}
