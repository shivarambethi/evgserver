package com.evgateway.server.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "owner_in_org")
@JsonIgnoreProperties(ignoreUnknown = true, value = { "hibernateLazyInitializer", "handler" })
public class OwnerInOrg extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long ownerId;

	private long orgId;

	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public OwnerInOrg(long ownerId, long orgId) {
		super();
		this.ownerId = ownerId;
		this.orgId = orgId;
	}

	public OwnerInOrg() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "OwnerInOrg [ownerId=" + ownerId + ", orgId=" + orgId + "]";
	}

}
