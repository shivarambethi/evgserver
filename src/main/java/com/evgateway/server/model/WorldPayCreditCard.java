package com.evgateway.server.model;

import java.util.Date;

public class WorldPayCreditCard {

	private static final long serialVersionUID = 1L;

	
	private long id;
	private Long accountId;
	private String PaymentAccountId;
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
	private Date createdDate;
	private Date modifiedDate;
	private String currencyType;
	private String paymentType;
	private String customerId;
	private String cardId;
	private String tokenId;

	public WorldPayCreditCard() {

	}

	public WorldPayCreditCard(Long accountId, String paymentAccountId, String cardNo, String cardName, String cardType,
			boolean defaultCard, String billingZipcode, String billingAddress1, String networkTransactionId,
			String transactionId, String expiryMonth, String expiryYear, boolean submissionTypeFlag) {
		super();
		this.accountId = accountId;
		PaymentAccountId = paymentAccountId;
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

	public String getPaymentAccountId() {
		return PaymentAccountId;
	}

	public void setPaymentAccountId(String paymentAccountId) {
		PaymentAccountId = paymentAccountId;
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

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
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

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
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

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	@Override
	public String toString() {
		return "WorldPayCreditCard [accountId=" + accountId + ", PaymentAccountId=" + PaymentAccountId + ", cardNo="
				+ cardNo + ", cardName=" + cardName + ", cardType=" + cardType + ", defaultCard=" + defaultCard
				+ ", billingZipcode=" + billingZipcode + ", billingAddress1=" + billingAddress1
				+ ", networkTransactionId=" + networkTransactionId + ", transactionId=" + transactionId
				+ ", expiryMonth=" + expiryMonth + ", expiryYear=" + expiryYear + ", submissionTypeFlag="
				+ submissionTypeFlag + ", createdDate=" + createdDate + ", modifiedDate=" + modifiedDate
				+ ", currencyType=" + currencyType + ", paymentType=" + paymentType + ", customerId=" + customerId
				+ ", cardId=" + cardId + ", tokenId=" + tokenId + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
