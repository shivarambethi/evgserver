package com.evgateway.server.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "user_in_manufacturer")
@JsonIgnoreProperties(ignoreUnknown = true, value = { "hibernateLazyInitializer", "handler" })
public class UserInManufacturer extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5315909669718681522L;

	private long userId;

	private long manufacturerId;

	public UserInManufacturer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserInManufacturer(long userId, long manufacturerId) {
		super();
		this.userId = userId;
		this.manufacturerId = manufacturerId;
	}

	public long getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(long manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "UserInManufacturer [userId=" + userId + ", manufacturerId=" + manufacturerId + "]";
	}

}
