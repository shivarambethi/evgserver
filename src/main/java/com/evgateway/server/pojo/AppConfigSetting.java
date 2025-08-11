package com.evgateway.server.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "app_config_setting")
public class AppConfigSetting extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private double RFIDPrice;

	private double rfidPrice1;

	private double FOBPrice;
	private double fobPrice1;
	 private String paymentMethod;
	private int RFIDLimitPerOrder;
	private int rfidLimitPerOrder1;

	private double payAsYouGoProcessingFee;

	private double payAsYouGoPreAuthorizationAmount;

	private long orgId;

	private double autoReloadMinAmount;

	private double autoReloadMaxAmount;

	private double addMoneyMinAmount;

	private double addMoneyMaxAmount;

	private double addMoneyOption1;

	private double addMoneyOption2;

	private double addMoneyOption3;

	private double lowBalance;

	@Column(name = "currencyCode")
	private String currencyCode;

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	private String currencySymbol;

	private Long gstId;
	@Temporal(TemporalType.DATE)
	@Column(name = "Modified_Date", length = 10)
	private Date modifiedDate;
	@Transient
	public double getRfidPrice1() {
		return rfidPrice1;
	}

	public void setRfidPrice1(double rfidPrice1) {
		this.rfidPrice1 = rfidPrice1;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	@Transient
	private String appVersion;

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public double getRFIDPrice() {
		return RFIDPrice;
	}

	public void setRFIDPrice(double rFIDPrice) {
		RFIDPrice = rFIDPrice;
	}

	public double getFOBPrice() {
		return FOBPrice;
	}

	public void setFOBPrice(double fOBPrice) {
		FOBPrice = fOBPrice;
	}

	public double getPayAsYouGoProcessingFee() {
		return payAsYouGoProcessingFee;
	}

	public void setPayAsYouGoProcessingFee(double payAsYouGoProcessingFee) {
		this.payAsYouGoProcessingFee = payAsYouGoProcessingFee;
	}

	public double getPayAsYouGoPreAuthorizationAmount() {
		return payAsYouGoPreAuthorizationAmount;
	}

	public void setPayAsYouGoPreAuthorizationAmount(double payAsYouGoPreAuthorizationAmount) {
		this.payAsYouGoPreAuthorizationAmount = payAsYouGoPreAuthorizationAmount;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public double getAutoReloadMinAmount() {
		return autoReloadMinAmount;
	}

	public void setAutoReloadMinAmount(double autoReloadMinAmount) {
		this.autoReloadMinAmount = autoReloadMinAmount;
	}

	public double getLowBalance() {
		return lowBalance;
	}

	public void setLowBalance(double lowBalance) {
		this.lowBalance = lowBalance;
	}

	public double getAddMoneyMinAmount() {
		return addMoneyMinAmount;
	}

	public void setAddMoneyMinAmount(double addMoneyMinAmount) {
		this.addMoneyMinAmount = addMoneyMinAmount;
	}

	public double getAddMoneyMaxAmount() {
		return addMoneyMaxAmount;
	}

	public void setAddMoneyMaxAmount(double addMoneyMaxAmount) {
		this.addMoneyMaxAmount = addMoneyMaxAmount;
	}

	public double getAddMoneyOption1() {
		return addMoneyOption1;
	}

	public void setAddMoneyOption1(double addMoneyOption1) {
		this.addMoneyOption1 = addMoneyOption1;
	}

	public double getAddMoneyOption2() {
		return addMoneyOption2;
	}

	public void setAddMoneyOption2(double addMoneyOption2) {
		this.addMoneyOption2 = addMoneyOption2;
	}

	public double getAddMoneyOption3() {
		return addMoneyOption3;
	}

	public void setAddMoneyOption3(double addMoneyOption3) {
		this.addMoneyOption3 = addMoneyOption3;
	}

	public double getAutoReloadMaxAmount() {
		return autoReloadMaxAmount;
	}

	public void setAutoReloadMaxAmount(double autoReloadMaxAmount) {
		this.autoReloadMaxAmount = autoReloadMaxAmount;
	}

	public Long getGstId() {
		return gstId;
	}

	public void setGstId(Long gstId) {
		this.gstId = gstId;
	}

	public int getRFIDLimitPerOrder() {
		return RFIDLimitPerOrder;
	}

	@Transient
	public double getFobPrice1() {
		return fobPrice1;
	}

	public void setFobPrice1(double fobPrice1) {
		this.fobPrice1 = fobPrice1;
	}

	public void setRFIDLimitPerOrder(int rFIDLimitPerOrder) {
		RFIDLimitPerOrder = rFIDLimitPerOrder;
	}

	
	@Transient
	public int getRfidLimitPerOrder1() {
		return rfidLimitPerOrder1;
	}

	public void setRfidLimitPerOrder1(int rfidLimitPerOrder1) {
		this.rfidLimitPerOrder1 = rfidLimitPerOrder1;
	}

	@Override
	public String toString() {
		return "AppConfigSetting [RFIDPrice=" + RFIDPrice + ", rfidPrice1=" + rfidPrice1 + ", FOBPrice=" + FOBPrice
				+ ", fobPrice1=" + fobPrice1 + ", paymentMethod=" + paymentMethod + ", RFIDLimitPerOrder="
				+ RFIDLimitPerOrder + ", rfidLimitPerOrder1=" + rfidLimitPerOrder1 + ", payAsYouGoProcessingFee="
				+ payAsYouGoProcessingFee + ", payAsYouGoPreAuthorizationAmount=" + payAsYouGoPreAuthorizationAmount
				+ ", orgId=" + orgId + ", autoReloadMinAmount=" + autoReloadMinAmount + ", autoReloadMaxAmount="
				+ autoReloadMaxAmount + ", addMoneyMinAmount=" + addMoneyMinAmount + ", addMoneyMaxAmount="
				+ addMoneyMaxAmount + ", addMoneyOption1=" + addMoneyOption1 + ", addMoneyOption2=" + addMoneyOption2
				+ ", addMoneyOption3=" + addMoneyOption3 + ", lowBalance=" + lowBalance + ", currencyCode="
				+ currencyCode + ", currencySymbol=" + currencySymbol + ", gstId=" + gstId + ", modifiedDate="
				+ modifiedDate + ", appVersion=" + appVersion + "]";
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

}
