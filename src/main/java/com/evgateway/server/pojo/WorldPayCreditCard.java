package com.evgateway.server.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "worldPay_creditCard")
public class WorldPayCreditCard extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	//private Long accounts;
	private String paymentAccountId;
	private String cardNo;
	private String cardName;
	private String cardType;
	private boolean defaultCard;
	private String billingZipcode;
	private String billingAddress1;
	private String networkTransactionId;
	private String transactionId;
	private String expiryMonth;
	private String expiryYear;
	private boolean submissionTypeFlag;
	private long accountId;
	private Date createdDate;
	private Date modifiedDate;
	private String paymentType;
	private String currencyType;
	private String cardId;
	private String customerId;
	private String reversalStatus = "";
	private int count = 0;
	private boolean flag;
	private String paymentGateway;
	
	

	public WorldPayCreditCard(Long accounts, String paymentAccountId, String cardNo, String cardName, String cardType,
			boolean defaultCard, String billingZipcode, String billingAddress1, String networkTransactionId,
			String transactionId, String expiryMonth, String expiryYear, boolean submissionTypeFlag) {
		super();
		this.accountId = accounts;
		this.paymentAccountId = paymentAccountId;
		this.cardNo = cardNo;
		this.cardName = cardName;
		this.cardType = cardType;
		this.defaultCard = defaultCard;
		this.billingZipcode = billingZipcode;
		this.billingAddress1 = billingAddress1;
		this.networkTransactionId = networkTransactionId;
		this.transactionId = transactionId;
		this.expiryMonth = expiryMonth;
		this.expiryYear = expiryYear;
		this.submissionTypeFlag = submissionTypeFlag;
	}
	public WorldPayCreditCard() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public Long getId() {
		return id;
	}
	@Override
	public void setId(Long id) {
		this.id = id;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public String getPaymentAccountId() {
		return paymentAccountId;
	}
	public void setPaymentAccountId(String paymentAccountId) {
		this.paymentAccountId = paymentAccountId;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	
	public boolean isDefaultCard() {
		return defaultCard;
	}
	public void setDefaultCard(boolean defaultCard) {
		this.defaultCard = defaultCard;
	}
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
	public String getBillingZipcode() {
		return billingZipcode;
	}
	public void setBillingZipcode(String billingZipcode) {
		this.billingZipcode = billingZipcode;
	}
	public String getBillingAddress1() {
		return billingAddress1;
	}
	public void setBillingAddress1(String billingAddress1) {
		this.billingAddress1 = billingAddress1;
	}
	public String getNetworkTransactionId() {
		return networkTransactionId;
	}
	public void setNetworkTransactionId(String networkTransactionId) {
		this.networkTransactionId = networkTransactionId;
	}
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public String getExpiryMonth() {
		return expiryMonth;
	}
	public void setExpiryMonth(String expiryMonth) {
		this.expiryMonth = expiryMonth;
	}
	public String getExpiryYear() {
		return expiryYear;
	}
	public void setExpiryYear(String expiryYear) {
		this.expiryYear = expiryYear;
	}
	
	public boolean isSubmissionTypeFlag() {
		return submissionTypeFlag;
	}
	public void setSubmissionTypeFlag(boolean submissionTypeFlag) {
		this.submissionTypeFlag = submissionTypeFlag;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	public String getCardId() {
		return cardId;
	}
	public void setCardId(String cardId) {
		this.cardId = cardId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getReversalStatus() {
		return reversalStatus;
	}
	public void setReversalStatus(String reversalStatus) {
		this.reversalStatus = reversalStatus;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Column(name="flag",columnDefinition = "double precision default 0 not null")
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getPaymentGateway() {
		return paymentGateway;
	}
	public void setPaymentGateway(String paymentGateway) {
		this.paymentGateway = paymentGateway;
	}

	
}
