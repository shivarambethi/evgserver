package com.evgateway.server.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "releaseNote")
public class ReleaseNote extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	@Column(name = "creation_date", length = 10)
	private Date creationDate = new Date();
	private String version;
	
	private boolean iscurrent;

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}	

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public boolean isIscurrent() {
		return iscurrent;
	}

	public void setIscurrent(boolean iscurrent) {
		this.iscurrent = iscurrent;
	}	

	@Override
	public String toString() {
		return "ReleaseNote [creationDate=" + creationDate + ", version=" + version + ",iscurrent=" + iscurrent + "]";
	}
}
