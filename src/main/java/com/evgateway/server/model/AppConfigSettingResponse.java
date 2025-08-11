package com.evgateway.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.evgateway.server.pojo.RecurringAmount;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.USE_DEFAULTS)
public class AppConfigSettingResponse implements Serializable {

	private long orgId;

	private double autoReloadMinAmount;

	private double autoReloadMaxAmount;

	private double addMoneyMinAmount;

	private double addMoneyMaxAmount;

	private double addMoneyOption1;

	private double addMoneyOption2;

	private double addMoneyOption3;

	private double lowBalance;
	private String currencyCode;

	private double recurringAmount;

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public double getAutoReloadMinAmount() {
		return autoReloadMinAmount;
	}

	public void setAutoReloadMinAmount(double autoReloadMinAmount) {
		this.autoReloadMinAmount = autoReloadMinAmount;
	}

	public double getAutoReloadMaxAmount() {
		return autoReloadMaxAmount;
	}

	public void setAutoReloadMaxAmount(double autoReloadMaxAmount) {
		this.autoReloadMaxAmount = autoReloadMaxAmount;
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

	public double getLowBalance() {
		return lowBalance;
	}

	public void setLowBalance(double lowBalance) {
		this.lowBalance = lowBalance;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public double getRecurringAmount() {
		return recurringAmount;
	}

	public void setRecurringAmount(double recurringAmount) {
		this.recurringAmount = recurringAmount;
	}

	@Override
	public String toString() {
		return "AppConfigSettingResponse [orgId=" + orgId + ", autoReloadMinAmount=" + autoReloadMinAmount
				+ ", autoReloadMaxAmount=" + autoReloadMaxAmount + ", addMoneyMinAmount=" + addMoneyMinAmount
				+ ", addMoneyMaxAmount=" + addMoneyMaxAmount + ", addMoneyOption1=" + addMoneyOption1
				+ ", addMoneyOption2=" + addMoneyOption2 + ", addMoneyOption3=" + addMoneyOption3 + ", lowBalance="
				+ lowBalance + ", currencyCode=" + currencyCode + ", recurringAmount=" + recurringAmount + "]";
	}

}