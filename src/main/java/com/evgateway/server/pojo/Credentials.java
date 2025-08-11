package com.evgateway.server.pojo;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "creadential")
@JsonIgnoreProperties(ignoreUnknown = true, value = { "account" })
public class Credentials extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String rfId;
	private String phone;
	private String rfidHex;
	private String uid;
	
	private String rfidName;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "expiryDate", length = 10)
	private Date expiryDate;

	private String status;

	private Accounts account;

	private String chargingObject;

	private String oldRefId;

	private long orgId;

	private String cardType;

	@Temporal(TemporalType.DATE)
	@Column(name = "creation_date", length = 10)
	private Date creationDate = new Date();

	@Temporal(TemporalType.DATE)
	@Column(name = "modified_date", length = 10)
	private Date modifiedDate;

	@Column
	private String createdBy;
	@Column
	private String lastModifiedBy;
	@Column
	private boolean ampFlag;

	public Credentials() {

	}

	public Credentials(String phone, Accounts account) {
		this.phone = phone;
		this.account = account;
		// this.orgId = orgId;
	}

	public String getRfId() {
		return rfId;
	}

	public void setRfId(String rfId) {
		this.rfId = rfId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRfidHex() {
		return rfidHex;
	}

	public void setRfidHex(String rfidHex) {
		this.rfidHex = rfidHex;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.MERGE })
	@JoinColumn(name = "account_id")
	public Accounts getAccount() {
		return account;
	}

	public void setAccount(Accounts account) {
		this.account = account;
	}

	public String getChargingObject() {
		return chargingObject;
	}

	public void setChargingObject(String chargingObject) {
		this.chargingObject = chargingObject;
	}

	public String getOldRefId() {
		return oldRefId;
	}

	public void setOldRefId(String oldRefId) {
		this.oldRefId = oldRefId;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public boolean isAmpFlag() {
		return ampFlag;
	}

	public void setAmpFlag(boolean ampFlag) {
		this.ampFlag = ampFlag;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	@Override
	public String toString() {
		return "Credentials [rfId=" + rfId + ", phone=" + phone + ", rfidHex=" + rfidHex + ", uid=" + uid
				+ ", expiryDate=" + expiryDate + ", status=" + status + ", account=" + account + ", chargingObject="
				+ chargingObject + ", oldRefId=" + oldRefId + ", orgId=" + orgId + ", cardType=" + cardType
				+ ", creationDate=" + creationDate + ", modifiedDate=" + modifiedDate + ", createdBy=" + createdBy
				+ ", lastModifiedBy=" + lastModifiedBy + ", ampFlag=" + ampFlag + "]";
	}

	public String getRfidName() {
		return rfidName;
	}

	public void setRfidName(String rfidName) {
		this.rfidName = rfidName;
	}



//	public String getRfidName() {
//		return rfidName;
//	}
//
//	public void setRfidName(String rfidName) {
//		this.rfidName = rfidName;
//	}
	
	

}
