package com.evgateway.server.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;



@Entity
@Table(name = "recurringAmount")
public class RecurringAmount extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	double amount;
	
	String currencyCode;

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

	@Override
	public String toString() {
		return "RecurringAmount [amount=" + amount + ", currencyCode=" + currencyCode + "]";
	}

}