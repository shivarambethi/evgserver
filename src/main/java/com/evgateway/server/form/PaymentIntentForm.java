package com.evgateway.server.form;

public class PaymentIntentForm {

	private String uuid;
	private long orgId;
	private double amount;
	private String currencyCode;
	private String paymentIntentId;
	private String clientSecret;
	private String customerId;
	private String ephemeralKey;
	private String transactionType;
	private String email;
	private String deviceName;
	private String deviceToken;
	private String deviceType;
	private String cardNo;
	private String phone;
	private long stationId;
	private long portId;
	private String orgName;
	private double captureAmount;
	private long userPaymentId;
	private String userType;
	private long accTxnId;
	private boolean defaultCard;
	private String cardId;
	private String bearerToken;
	private String paymentMethodId;
	
	private String countryCode;
	
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getPaymentIntentId() {
		return paymentIntentId;
	}

	public void setPaymentIntentId(String paymentIntentId) {
		this.paymentIntentId = paymentIntentId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getEphemeralKey() {
		return ephemeralKey;
	}

	public void setEphemeralKey(String ephemeralKey) {
		this.ephemeralKey = ephemeralKey;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public long getStationId() {
		return stationId;
	}

	public void setStationId(long stationId) {
		this.stationId = stationId;
	}

	public long getPortId() {
		return portId;
	}

	public void setPortId(long portId) {
		this.portId = portId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public double getCaptureAmount() {
		return captureAmount;
	}

	public void setCaptureAmount(double captureAmount) {
		this.captureAmount = captureAmount;
	}

	public long getUserPaymentId() {
		return userPaymentId;
	}

	public void setUserPaymentId(long userPaymentId) {
		this.userPaymentId = userPaymentId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public long getAccTxnId() {
		return accTxnId;
	}

	public void setAccTxnId(long accTxnId) {
		this.accTxnId = accTxnId;
	}

	public boolean isDefaultCard() {
		return defaultCard;
	}

	public void setDefaultCard(boolean defaultCard) {
		this.defaultCard = defaultCard;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getBearerToken() {
		return bearerToken;
	}

	public void setBearerToken(String bearerToken) {
		this.bearerToken = bearerToken;
	}

	public String getPaymentMethodId() {
		return paymentMethodId;
	}

	public void setPaymentMethodId(String paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

	@Override
	public String toString() {
		return "PaymentIntentForm [uuid=" + uuid + ", orgId=" + orgId + ", amount=" + amount + ", currencyCode="
				+ currencyCode + ", paymentIntentId=" + paymentIntentId + ", clientSecret=" + clientSecret
				+ ", customerId=" + customerId + ", ephemeralKey=" + ephemeralKey + ", transactionType="
				+ transactionType + ", email=" + email + ", deviceName=" + deviceName + ", deviceToken=" + deviceToken
				+ ", deviceType=" + deviceType + ", cardNo=" + cardNo + ", phone=" + phone + ", stationId=" + stationId
				+ ", portId=" + portId + ", orgName=" + orgName + ", captureAmount=" + captureAmount
				+ ", userPaymentId=" + userPaymentId + ", userType=" + userType + ", accTxnId=" + accTxnId
				+ ", defaultCard=" + defaultCard + ", cardId=" + cardId + ", bearerToken=" + bearerToken
				+ ", paymentMethodId=" + paymentMethodId + "]";
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

}
