package com.evgateway.server.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "dealer_in_dealerAdmin")
@JsonIgnoreProperties(ignoreUnknown = true, value = { "hibernateLazyInitializer", "handler" })
public class DealerUnderDealerAdmin extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long dealerId;

	private long dealerAdminId;

	public DealerUnderDealerAdmin(long dealerId, long dealerAdminId) {
		super();
		this.dealerId = dealerId;
		this.dealerAdminId = dealerAdminId;
	}

	public DealerUnderDealerAdmin() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getDealerAdminId() {
		return dealerAdminId;
	}

	public void setDealerAdminId(long dealerAdminId) {
		this.dealerAdminId = dealerAdminId;
	}

	public long getDealerId() {
		return dealerId;
	}

	public void setDealerId(long dealerId) {
		this.dealerId = dealerId;
	}

	@Override
	public String toString() {
		return "DealerUnderDealerAdmin [dealerId=" + dealerId + ", dealerAdminId=" + dealerAdminId + "]";
	}

}
