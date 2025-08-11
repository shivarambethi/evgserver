package com.evgateway.server.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "touprofile_for_mobile")
public class TOUProfile_For_Mobile extends BaseEntity {

	private static final long serialVersionUID = -6872245135392122295L;

	private double energy;
	private double time;
	private double flat;
	private double parking;
	private double rateRider;
	private long timeStepSize;
	private long parkingStepSize;
	private String tariff;
	private long tariff_id;
	private String tax1;
	private String tax2;
	private String type;
	private double tax1_percentage;
	private double tax2_percentage;

	@Type(type = "json")
	@Column(columnDefinition = "TEXT")
	private String day;
	private String month;
	private String startTime;
	private String endTime;
	private String pricing_type;
	private String startDate;
	private String endDate;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getEnergy() {
		return energy;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public double getFlat() {
		return flat;
	}

	public void setFlat(double flat) {
		this.flat = flat;
	}

	public double getParking() {
		return parking;
	}

	public void setParking(double parking) {
		this.parking = parking;
	}

	public double getRateRider() {
		return rateRider;
	}

	public void setRateRider(double rateRider) {
		this.rateRider = rateRider;
	}

	public long getTimeStepSize() {
		return timeStepSize;
	}

	public void setTimeStepSize(long timeStepSize) {
		this.timeStepSize = timeStepSize;
	}

	public long getParkingStepSize() {
		return parkingStepSize;
	}

	public void setParkingStepSize(long parkingStepSize) {
		this.parkingStepSize = parkingStepSize;
	}

	public String getTariff() {
		return tariff;
	}

	public void setTariff(String tariff) {
		this.tariff = tariff;
	}

	public long getTariff_id() {
		return tariff_id;
	}

	public void setTariff_id(long tariff_id) {
		this.tariff_id = tariff_id;
	}

	public String getTax1() {
		return tax1;
	}

	public void setTax1(String tax1) {
		this.tax1 = tax1;
	}

	public String getTax2() {
		return tax2;
	}

	public void setTax2(String tax2) {
		this.tax2 = tax2;
	}

	public double getTax1_percentage() {
		return tax1_percentage;
	}

	public void setTax1_percentage(double tax1_percentage) {
		this.tax1_percentage = tax1_percentage;
	}

	public double getTax2_percentage() {
		return tax2_percentage;
	}

	public void setTax2_percentage(double tax2_percentage) {
		this.tax2_percentage = tax2_percentage;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
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

	public String getPricing_type() {
		return pricing_type;
	}

	public void setPricing_type(String pricing_type) {
		this.pricing_type = pricing_type;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "TOUProfile_For_Mobile [energy=" + energy + ", time=" + time + ", flat=" + flat + ", parking=" + parking
				+ ", rateRider=" + rateRider + ", timeStepSize=" + timeStepSize + ", parkingStepSize=" + parkingStepSize
				+ ", tariff=" + tariff + ", tariff_id=" + tariff_id + ", tax1=" + tax1 + ", tax2=" + tax2 + ", type="
				+ type + ", tax1_percentage=" + tax1_percentage + ", tax2_percentage=" + tax2_percentage + ", day="
				+ day + ", month=" + month + ", startTime=" + startTime + ", endTime=" + endTime + ", pricing_type="
				+ pricing_type + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}

}
