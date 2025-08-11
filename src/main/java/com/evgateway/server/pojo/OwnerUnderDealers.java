package com.evgateway.server.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "owner_in_dealer")
@JsonIgnoreProperties(ignoreUnknown = true, value = { "hibernateLazyInitializer", "handler" })
public class OwnerUnderDealers extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long ownerId;

	private long dealerId;

	public OwnerUnderDealers(long ownerId, long dealerId) {
		super();
		this.ownerId = ownerId;
		this.dealerId = dealerId;
	}

	
	
	public OwnerUnderDealers() {
		super();
		// TODO Auto-generated constructor stub
	}



	public long getDealerId() {
		return dealerId;
	}

	public void setDealerId(long dealerId) {
		this.dealerId = dealerId;
	}

	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}

	@Override
	public String toString() {
		return "OwnerUnderDealers [ownerId=" + ownerId + ", dealerId=" + dealerId + "]";
	}

}
