package com.evgateway.server.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VehicleForm {

	private Long accountId;
	private String year;
	private String make;
	private String model;
	private String connectorType;
	private String vehicle_type;
	private String description;
	private String vin;
	private String uid;
	private String pluginType;
	private double capacitykWh;
	

	public String getPluginType() {
		return pluginType;
	}

	public void setPluginType(String pluginType) {
		this.pluginType = pluginType;
	}

	

	public double getCapacitykWh() {
		return capacitykWh;
	}

	public void setCapacitykWh(double capacitykWh) {
		this.capacitykWh = capacitykWh;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	// for delete and editEV
	private Long vehicleId;

	
	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getConnectorType() {
		return connectorType;
	}

	public void setConnectorType(String connectorType) {
		this.connectorType = connectorType;
	}

	public Long getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Long vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	@Override
	public String toString() {
		return "VehicleForm [accountId=" + accountId + ", year=" + year + ", make=" + make + ", model=" + model
				+ ", connectorType=" + connectorType + ", vehicle_type=" + vehicle_type + ", description=" + description
				+ ", vin=" + vin + ", uid=" + uid + ", pluginType=" + pluginType + ", capacitykWh="
				+ capacitykWh + ", vehicleId=" + vehicleId + "]";
	}

	public String getVehicle_type() {
		return vehicle_type;
	}

	public void setVehicle_type(String vehicle_type) {
		this.vehicle_type = vehicle_type;
	}

}
