package com.evgateway.server.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "dealer_in_org")
@JsonIgnoreProperties(ignoreUnknown = true, value = { "hibernateLazyInitializer", "handler" })
public class DealerInOrg extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long dealerId;

	private long orgId;

	public DealerInOrg(long dealerId, long orgId) {
		super();
		this.dealerId = dealerId;
		this.orgId = orgId;
	}

	public DealerInOrg() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getDealerId() {
		return dealerId;
	}

	public void setDealerId(long dealerId) {
		this.dealerId = dealerId;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

}
