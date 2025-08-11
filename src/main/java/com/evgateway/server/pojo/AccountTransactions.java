package com.evgateway.server.pojo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "account_transaction")
@JsonIgnoreProperties(ignoreUnknown = true, value = { "account", "sessions" })
public class AccountTransactions extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	@Column(name = "createTimeStamp", length = 10)
	private Date createTimeStamp;

	private double amtDebit;
	private double amtCredit;
	private double currentBalance;

	private String status;
	private String comment;

	private String customerId;
	private Long customerIdAtStationType;

	private String oldRefId;

	private Accounts account;

	private String transactionId;

	@Column(name = "CGST_Debit")
	private double CGST_Debit;
	@Column(name = "SGST_Debit")
	private double SGST_Debit;

	private double rechargeAmount;

	@Temporal(TemporalType.DATE)
	@Column(name = "creation_date", length = 10)
	private Date creationDate = new Date();

	@Temporal(TemporalType.DATE)
	@Column(name = "modified_date", length = 10)
	private Date modifiedDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "lastUpdatedTime", length = 10)
	private Date lastUpdatedTime = new Date();

	public Date getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(Date lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

	@Column
	private String createdBy;
	@Column
	private String lastModifiedBy;

	@JsonIgnore
	private Set<Session> sessions = new HashSet<Session>(0);

	private String currencyType;

	private String transactionType;

	private String paymentMode;

	private String cardNo;

	private String cardType;

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

	public Date getCreateTimeStamp() {
		return createTimeStamp;
	}

	public void setCreateTimeStamp(Date createTimeStamp) {
		this.createTimeStamp = createTimeStamp;
	}

	public double getAmtDebit() {
		return amtDebit;
	}

	public void setAmtDebit(double amtDebit) {
		this.amtDebit = amtDebit;
	}

	public double getAmtCredit() {
		return amtCredit;
	}

	public void setAmtCredit(double amtCredit) {
		this.amtCredit = amtCredit;
	}

	public double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.MERGE })
	@JoinColumn(name = "account_id")
	public Accounts getAccount() {
		return account;
	}

	public void setAccount(Accounts account) {
		this.account = account;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "accountTransaction")
	public Set<Session> getSessions() {
		return sessions;
	}

	public void setSessions(Set<Session> sessions) {
		this.sessions = sessions;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Long getCustomerIdAtStationType() {
		return customerIdAtStationType;
	}

	public void setCustomerIdAtStationType(Long customerIdAtStationType) {
		this.customerIdAtStationType = customerIdAtStationType;
	}

	public String getOldRefId() {
		return oldRefId;
	}

	public void setOldRefId(String oldRefId) {
		this.oldRefId = oldRefId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public double getCGST_Debit() {
		return CGST_Debit;
	}

	public void setCGST_Debit(double cGST_Debit) {
		CGST_Debit = cGST_Debit;
	}

	public double getSGST_Debit() {
		return SGST_Debit;
	}

	public void setSGST_Debit(double sGST_Debit) {
		SGST_Debit = sGST_Debit;
	}

	public double getRechargeAmount() {
		return rechargeAmount;
	}

	public void setRechargeAmount(double rechargeAmount) {
		this.rechargeAmount = rechargeAmount;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getPaymentMode() {
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	@Override
	public String toString() {
		return "AccountTransactions [createTimeStamp=" + createTimeStamp + ", amtDebit=" + amtDebit + ", amtCredit="
				+ amtCredit + ", currentBalance=" + currentBalance + ", status=" + status + ", comment=" + comment
				+ ", customerId=" + customerId + ", customerIdAtStationType=" + customerIdAtStationType + ", oldRefId="
				+ oldRefId + ", account=" + account + ", transactionId=" + transactionId + ", CGST_Debit=" + CGST_Debit
				+ ", SGST_Debit=" + SGST_Debit + ", rechargeAmount=" + rechargeAmount + ", creationDate=" + creationDate
				+ ", modifiedDate=" + modifiedDate + ", createdBy=" + createdBy + ", lastModifiedBy=" + lastModifiedBy
				+ ", sessions=" + sessions + ", currencyType=" + currencyType + ", transactionType=" + transactionType
				+ ", paymentMode=" + paymentMode + ", cardNo=" + cardNo + ", cardType=" + cardType + "]";
	}

}