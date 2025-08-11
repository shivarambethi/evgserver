package com.evgateway.server.pojo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "resettoken_of_user")
public class ForgetPasswordUser extends BaseEntity {

	private static final long serialVersionUID = -6872245135392122295L;

	private String resetToken;
	private String userUID;
	private Date creationDate;
	private Date updateDate;

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getResetToken() {
		return resetToken;
	}

	public void setResetToken(String resetToken) {
		this.resetToken = resetToken;
	}

	public String getUserUID() {
		return userUID;
	}

	public void setUserUID(String userUID) {
		this.userUID = userUID;
	}

	@Override
	public String toString() {
		return "ForgetPasswordUser [resetToken=" + resetToken + ", userUID=" + userUID + ", creationDate="
				+ creationDate + ", updateDate=" + updateDate + "]";
	}

}
