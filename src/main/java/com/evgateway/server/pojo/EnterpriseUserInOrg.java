package com.evgateway.server.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "enterpriseuser_in_org")
@JsonIgnoreProperties(ignoreUnknown = true, value = { "hibernateLazyInitializer", "handler" })
public class EnterpriseUserInOrg extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5315909669718681522L;

	private long orgId;

	private long enterpriseId;

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public long getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(long enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public EnterpriseUserInOrg(long orgId, long enterpriseId) {
		super();
		this.orgId = orgId;
		this.enterpriseId = enterpriseId;
	}

	public EnterpriseUserInOrg() {
		super();
		
	}

	@Override
	public String toString() {
		return "EnterpriseUserInOrg [orgId=" + orgId + ", enterpriseId=" + enterpriseId + "]";
	}

}
