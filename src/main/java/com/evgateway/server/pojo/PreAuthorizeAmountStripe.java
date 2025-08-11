package com.evgateway.server.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "preAuthorize_amountStripe")
public class PreAuthorizeAmountStripe extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	@Column(name = "authorizeTimeStamp", length = 10)
	private Date authorizeTimeStamp;

	@Temporal(TemporalType.DATE)
	@Column(name = "capturedTimeStamp", length = 10)
	private Date capturedTimeStamp;

	private long id;
	private long userId;
	private long accountId;
	private String chargeCaptureId;
	private String phone;
	private String email;
	private double authorizeAmount;// 40$
	private String stripeToken;
	private double capturedAmount; // revenue
	private boolean flagValue;
	private String deviceToken;
	private String deviceName;
	private long stationId;
	private long portId;
	private long orgId;
	private String deviceType;
	private String cardNumber;
	private String transactionId;
	private String cardType;
	private String billingAddress;
	private String zipcode;
	private String paymentMethod;
	private String reversalStatus = "";
	private String finalStatus;
	private double reversalAmount; // refund
	private String currencyType;
	private String creditAmountStatus = "";
	private int count;

	public double getAuthorizeAmount() {
		return authorizeAmount;
	}

	public void setAuthorizeAmount(double authorizeAmount) {
		this.authorizeAmount = authorizeAmount;
	}



	public Date getAuthorizeTimeStamp() {
		return authorizeTimeStamp;
	}

	public void setAuthorizeTimeStamp(Date authorizeTimeStamp) {
		this.authorizeTimeStamp = authorizeTimeStamp;
	}

	public double getCapturedAmount() {
		return capturedAmount;
	}

	public void setCapturedAmount(double capturedAmount) {
		this.capturedAmount = capturedAmount;
	}

	public Date getCapturedTimeStamp() {
		return capturedTimeStamp;
	}

	public void setCapturedTimeStamp(Date capturedTimeStamp) {
		this.capturedTimeStamp = capturedTimeStamp;
	}

	public String getChargeCaptureId() {
		return chargeCaptureId;
	}

	public void setChargeCaptureId(String chargeCaptureId) {
		this.chargeCaptureId = chargeCaptureId;
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

	public String getStripeToken() {
		return stripeToken;
	}

	public void setStripeToken(String stripeToken) {
		this.stripeToken = stripeToken;
	}

	public boolean isFlagValue() {
		return flagValue;
	}

	public void setFlagValue(boolean flagValue) {
		this.flagValue = flagValue;
	}

	public long getPortId() {
		return portId;
	}

	public void setPortId(long portId) {
		this.portId = portId;
	}

	public long getStationId() {
		return stationId;
	}

	public void setStationId(long stationId) {
		this.stationId = stationId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getBillingAddress() {
		return billingAddress;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getReversalStatus() {
		return reversalStatus;
	}

	public void setReversalStatus(String reversalStatus) {
		this.reversalStatus = reversalStatus;
	}

	public String getFinalStatus() {
		return finalStatus;
	}

	public void setFinalStatus(String finalStatus) {
		this.finalStatus = finalStatus;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public double getReversalAmount() {
		return reversalAmount;
	}

	public void setReversalAmount(double reversalAmount) {
		this.reversalAmount = reversalAmount;
	}

	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	public String getCreditAmountStatus() {
		return creditAmountStatus;
	}

	public void setCreditAmountStatus(String creditAmountStatus) {
		this.creditAmountStatus = creditAmountStatus;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "PreAuthorizeAmountStripe [authorizeTimeStamp=" + authorizeTimeStamp + ", capturedTimeStamp="
				+ capturedTimeStamp + ", id=" + id + ", userId=" + userId + ", accountId=" + accountId
				+ ", chargeCaptureId=" + chargeCaptureId + ", phone=" + phone + ", email=" + email
				+ ", authorizeAmount=" + authorizeAmount + ", stripeToken=" + stripeToken + ", capturedAmount="
				+ capturedAmount + ", flagValue=" + flagValue + ", deviceToken=" + deviceToken + ", deviceName="
				+ deviceName + ", stationId=" + stationId + ", portId=" + portId + ", orgId=" + orgId + ", deviceType="
				+ deviceType + ", cardNumber=" + cardNumber + ", transactionId=" + transactionId + ", cardType="
				+ cardType + ", billingAddress=" + billingAddress + ", zipcode=" + zipcode + ", paymentMethod="
				+ paymentMethod + ", reversalStatus=" + reversalStatus + ", finalStatus=" + finalStatus
				+ ", reversalAmount=" + reversalAmount + ", currencyType=" + currencyType + ", creditAmountStatus="
				+ creditAmountStatus + ", count=" + count + "]";
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

}
