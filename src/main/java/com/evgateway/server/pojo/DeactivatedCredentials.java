package com.evgateway.server.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "deactivatedcredentials")
//@JsonIgnoreProperties(ignoreUnknown = true, value = { "accounts" })
public class DeactivatedCredentials extends BaseEntity {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long chargingObjectId;
	private String rfid;
	private String phone;
	private long accId;
	private Date deactivationTime;
	private String cardType;
	private String cardName;
	private long orgId;
	private String status;
	private String rfidHex;
	
/*
	public DeactivatedCredentials(Long chargingObjectId) {
		this.chargingObjectId = chargingObjectId;
	}

	public DeactivatedCredentials(Long chargingObjectId, Long rfid, String phone) {
		this.chargingObjectId = chargingObjectId;
		this.rfid = rfid;
		this.phone = phone;

	}*/
	
	public DeactivatedCredentials() {
		
	}
	
	public DeactivatedCredentials(String rfid,String rfidHex, Long accId, String cardType, long orgId,String status) {
		// TODO Auto-generated constructor stub
		this.accId = accId;
		this.rfid = rfid;
		this.rfidHex = rfidHex;
		this.cardType = cardType;
		this.setOrgId(orgId);
		this.status = status;
		 
	}

	//@Column(name = "chargingObjectid",nullable = true)
	public long getChargingObjectId() {
		return this.chargingObjectId;
	}

	public void setChargingObjectId(long chargingObjectId) {
		this.chargingObjectId = chargingObjectId;
	}

	
	public String getRfid() {
		return this.rfid;
	}

	public void setRfid(String rfid) {
		this.rfid = rfid;
	}

	@Column(name = "phone", length = 20)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	
	
	public long getAccId() {
		return accId;
	}

	public void setAccId(long accId) {
		this.accId = accId;
	}

	public Date getDeactivationTime() {
		return deactivationTime;
	}

	public void setDeactivationTime(Date deactivationTime) {
		this.deactivationTime = deactivationTime;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	
	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	
	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRfidHex() {
		return rfidHex;
	}

	public void setRfidHex(String rfidHex) {
		this.rfidHex = rfidHex;
	}

	@Override
	public String toString() {
		return "DeactivatedCredentials [chargingObjectId=" + chargingObjectId + ", rfid=" + rfid + ", phone=" + phone
				+ ", accId=" + accId + ", deactivationTime=" + deactivationTime + ", cardType=" + cardType
				+ ", cardName=" + cardName + ", orgId=" + orgId + ", status=" + status + ", rfidHex=" + rfidHex + "]";
	}
	
}
