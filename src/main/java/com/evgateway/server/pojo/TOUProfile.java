package com.evgateway.server.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "tou_profile")
@Entity
public class TOUProfile extends BaseEntityString {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long dynamicprofileId;
	private String profileName;
	private double standardPrice;
	private String standardPriceUnit;
	private String month;
	private String day;
	private String startTime;
	private String endTime;
	private double price;
	private String priceUnit;
	private String chargerType;
	

	public long getDynamicprofileId() {
		return dynamicprofileId;
	}

	public void setDynamicprofileId(long dynamicprofileId) {
		this.dynamicprofileId = dynamicprofileId;
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public double getStandardPrice() {
		return standardPrice;
	}

	public void setStandardPrice(double standardPrice) {
		this.standardPrice = standardPrice;
	}

	public String getStandardPriceUnit() {
		return standardPriceUnit;
	}

	public void setStandardPriceUnit(String standardPriceUnit) {
		this.standardPriceUnit = standardPriceUnit;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getPriceUnit() {
		return priceUnit;
	}

	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
	}

	public String getChargerType() {
		return chargerType;
	}

	public void setChargerType(String chargerType) {
		this.chargerType = chargerType;
	}

	
	@Override
	public String toString() {
		return "TOUProfile [dynamicprofileId=" + dynamicprofileId + ", profileName=" + profileName + ", standardPrice="
				+ standardPrice + ", standardPriceUnit=" + standardPriceUnit + ", month=" + month + ", day=" + day
				+ ", startTime=" + startTime + ", endTime=" + endTime + ", price=" + price + ", priceUnit=" + priceUnit
				+ ", chargerType=" + chargerType + "]";
	}

}
