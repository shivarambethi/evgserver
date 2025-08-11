package com.evgateway.server.pojo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "users_otp")
public class UsersOTP extends BaseEntity {

	private static final long serialVersionUID = -6872245135392122295L;

	private String userUID;

	private String otp;

	private String creationDate;

	private String updatedDate;

	public String getUserUID() {
		return userUID;
	}

	public void setUserUID(String userUID) {
		this.userUID = userUID;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Override
	public String toString() {
		return "UsersOTP [userUID=" + userUID + ", otp=" + otp + ", creationDate=" + creationDate + ", updatedDate="
				+ updatedDate + "]";
	}

	public UsersOTP(String userUID, String otp, String creationDate, String updatedDate) {
		super();
		this.userUID = userUID;
		this.otp = otp;
		this.creationDate = creationDate;
		this.updatedDate = updatedDate;
	}
	
	public UsersOTP(String userUID, String otp, String creationDate) {
		super();
		this.userUID = userUID;
		this.otp = otp;
		this.creationDate = creationDate;
	}

	public UsersOTP() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	

	
	
	

}
