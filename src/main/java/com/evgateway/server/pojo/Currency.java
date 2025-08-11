package com.evgateway.server.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "currency")
public class Currency extends BaseEntity {

	private static final long serialVersionUID = -6872245135392122295L;
	
	private String currencyType;
	private String currencySymbol;
	
	@Column(name="currencyType")
	public String getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}
	
	@Column(name="currencySymbol")
	public String getCurrencySymbol() {
		return currencySymbol;
	}
	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}
	
	@Override
	public String toString() {
		return "Currency [currencyType=" + currencyType + ", currencySymbol=" + currencySymbol + "]";
	}
	
}
