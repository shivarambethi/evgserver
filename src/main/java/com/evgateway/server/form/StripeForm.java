package com.evgateway.server.form;

public class StripeForm {

	private String uuid;
	private String paymentId;
	private String cardNo;
	private String cardName;
	private String cardType;
	private boolean defaultCard;
	private String billingZipcode;
	private String billingAddress1;
	private String expiryMonth;
	private String expiryYear;
	private double amount;
	private Long orgId;
	private String orgName;
	private String tokenId;
	private String currencyCode;
	private String cardId;
	private String customerId;
	private boolean autoReloadFlag;
	private Long stripeChargeId;
	private long accountTransId;
	private String amtDebitType;
	private String chargeId;
	private String bearerToken;

	public String getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
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
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Long getOrgId() {
		return orgId;
	}
	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public String getCurrencyCode() {
		return currencyCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
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
	public boolean isAutoReloadFlag() {
		return autoReloadFlag;
	}
	public void setAutoReloadFlag(boolean autoReloadFlag) {
		this.autoReloadFlag = autoReloadFlag;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Long getStripeChargeId() {
		return stripeChargeId;
	}
	public void setStripeChargeId(Long stripeChargeId) {
		this.stripeChargeId = stripeChargeId;
	}
	public long getAccountTransId() {
		return accountTransId;
	}
	public void setAccountTransId(long accountTransId) {
		this.accountTransId = accountTransId;
	}
	public String getAmtDebitType() {
		return amtDebitType;
	}
	public void setAmtDebitType(String amtDebitType) {
		this.amtDebitType = amtDebitType;
	}
	public String getChargeId() {
		return chargeId;
	}
	public void setChargeId(String chargeId) {
		this.chargeId = chargeId;
	}
	@Override
	public String toString() {
		return "StripeForm [uuid=" + uuid + ", paymentId=" + paymentId + ", cardNo=" + cardNo + ", cardName=" + cardName
				+ ", cardType=" + cardType + ", defaultCard=" + defaultCard + ", billingZipcode=" + billingZipcode
				+ ", billingAddress1=" + billingAddress1 + ", expiryMonth=" + expiryMonth + ", expiryYear=" + expiryYear
				+ ", amount=" + amount + ", orgId=" + orgId + ", orgName=" + orgName + ", tokenId=" + tokenId
				+ ", currencyCode=" + currencyCode + ", cardId=" + cardId + ", customerId=" + customerId
				+ ", autoReloadFlag=" + autoReloadFlag + ", stripeChargeId=" + stripeChargeId + ", accountTransId="
				+ accountTransId + ", amtDebitType=" + amtDebitType + ", chargeId=" + chargeId + ", bearerToken="
				+ bearerToken + "]";
	}
	public String getBearerToken() {
		return bearerToken;
	}
	public void setBearerToken(String bearerToken) {
		this.bearerToken = bearerToken;
	}

}
