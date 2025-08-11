package com.evgateway.server.pojo;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "vehicles")
@JsonIgnoreProperties(ignoreUnknown = true, value = "account")
public class Vehicles extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String oldRefId;
	private String vin;
	private String year;
	private String make;
	private String model;
	private String description;
	private String connectorType;
	private String status;
	private String vehicleType;
	private double batteryCapacity;

	private double capacitykWh;

	private String pluginType;

	private String uid;

	private long user_id;

	private Accounts account;
	
	

	public String getVin() {
		return this.vin;
	}

	@Column(name = "batteryCapacity", columnDefinition = "double precision default 0 not null")
	public double getBatteryCapacity() {
		return batteryCapacity;
	}

	public void setBatteryCapacity(double batteryCapacity) {
		this.batteryCapacity = batteryCapacity;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getYear() {
		return this.year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMake() {
		return this.make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getConnectorType() {
		return connectorType;
	}

	public void setConnectorType(String connectorType) {
		this.connectorType = connectorType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH, CascadeType.MERGE })
	@JoinColumn(name = "account_id")
	public Accounts getAccount() {
		return account;
	}

	public void setAccount(Accounts account) {
		this.account = account;
	}

	public String getOldRefId() {
		return oldRefId;
	}

	public void setOldRefId(String oldRefId) {
		this.oldRefId = oldRefId;
	}
	
	@Column(name = "capacitykWh", columnDefinition = "double precision default 0 not null")
	public double getCapacitykWh() {
		return capacitykWh;
	}

	public void setCapacitykWh(double capacitykWh) {
		this.capacitykWh = capacitykWh;
	}

	public String getPluginType() {
		return pluginType;
	}

	public void setPluginType(String pluginType) {
		this.pluginType = pluginType;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	@Column(name = "user_id", columnDefinition = "double precision default 0 not null")
	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	@Override
	public String toString() {
		return "Vehicles [oldRefId=" + oldRefId + ", vin=" + vin + ", year=" + year + ", make=" + make + ", model="
				+ model + ", description=" + description + ", connectorType=" + connectorType + ", status=" + status
				+ ", vehicleType=" + vehicleType + ", batteryCapacity=" + batteryCapacity + ", capacitykWh="
				+ capacitykWh + ", pluginType=" + pluginType + ", uid=" + uid + ", user_id=" + user_id + ", account="
				+ account + "]";
	}

}
