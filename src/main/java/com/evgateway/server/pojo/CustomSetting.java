package com.evgateway.server.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "CustomSetting")
public class CustomSetting extends BaseEntity {
	private static final long serialVersionUID = 1L;
	private long orgId;
	private String primaryColor;
	private String secondaryColor;
	private String url;

	private long groupId;

	@Transient
	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public String getPrimaryColor() {
		return primaryColor;
	}

	public void setPrimaryColor(String primaryColor) {
		this.primaryColor = primaryColor;
	}

	public String getSecondaryColor() {
		return secondaryColor;
	}

	public void setSecondaryColor(String secondaryColor) {
		this.secondaryColor = secondaryColor;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return "CustomSetting [orgId=" + orgId + ", primaryColor=" + primaryColor + ", secondaryColor=" + secondaryColor
				+ ", url=" + url + ", groupId=" + groupId + "]";
	}

}
