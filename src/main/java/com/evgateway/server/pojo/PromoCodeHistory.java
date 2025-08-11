package com.evgateway.server.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "promoCodeHistory")
@JsonIgnoreProperties(ignoreUnknown = true, value = { "" })
public class PromoCodeHistory extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String promoCode;
	private long promoId;
	private String amountType;
	private double amount;
	private long userId;
	private long orgId;
	private String email;
	private String transactionReferenceId;
	@Temporal(TemporalType.DATE)
	@Column(name = "creation_date", length = 10)
	private Date creationDate;
	private boolean flag;

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	public long getPromoId() {
		return promoId;
	}

	public void setPromoId(long promoId) {
		this.promoId = promoId;
	}

	public String getAmountType() {
		return amountType;
	}

	public void setAmountType(String amountType) {
		this.amountType = amountType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public long getUserId() {
		return userId;
	}

	@Column(name = "flag", columnDefinition = "bit default 0 not null")
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getTransactionReferenceId() {
		return transactionReferenceId;
	}

	public void setTransactionReferenceId(String transactionReferenceId) {
		this.transactionReferenceId = transactionReferenceId;
	}

	@Override
	public String toString() {
		return "PromoCodeHistory [promoCode=" + promoCode + ", promoId=" + promoId + ", amountType=" + amountType
				+ ", amount=" + amount + ", userId=" + userId + ", orgId=" + orgId + ", email=" + email
				+ ", transactionReferenceId=" + transactionReferenceId + ", creationDate=" + creationDate + ", flag="
				+ flag + "]";
	}

}
