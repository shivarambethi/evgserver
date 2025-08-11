package com.evgateway.server.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "assets")
public class Assets extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4533480750326177626L;

	private long refrenceId;

	private String refrenceName;

	private String type;
	private String make;
	private String model;
	private double capacity;
	private double energyStorage;
	private double energyGenerationCapacity;
	private String searialNumber;
	private String name;
	private int noOfBreakers;
	private String description;

	private String carrier;
	private String simNumber;
	private String breakerName;
	private int breakerNumber;
	private String typeValue;

	public long getRefrenceId() {
		return refrenceId;
	}

	public void setRefrenceId(long refrenceId) {
		this.refrenceId = refrenceId;
	}

	public String getRefrenceName() {
		return refrenceName;
	}

	public void setRefrenceName(String refrenceName) {
		this.refrenceName = refrenceName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public double getCapacity() {
		return capacity;
	}

	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	public double getEnergyStorage() {
		return energyStorage;
	}

	public void setEnergyStorage(double energyStorage) {
		this.energyStorage = energyStorage;
	}

	public double getEnergyGenerationCapacity() {
		return energyGenerationCapacity;
	}

	public void setEnergyGenerationCapacity(double energyGenerationCapacity) {
		this.energyGenerationCapacity = energyGenerationCapacity;
	}

	public String getSearialNumber() {
		return searialNumber;
	}

	public void setSearialNumber(String searialNumber) {
		this.searialNumber = searialNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNoOfBreakers() {
		return noOfBreakers;
	}

	public void setNoOfBreakers(int noOfBreakers) {
		this.noOfBreakers = noOfBreakers;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public String getSimNumber() {
		return simNumber;
	}

	public void setSimNumber(String simNumber) {
		this.simNumber = simNumber;
	}

	public String getBreakerName() {
		return breakerName;
	}

	public void setBreakerName(String breakerName) {
		this.breakerName = breakerName;
	}

	public int getBreakerNumber() {
		return breakerNumber;
	}

	public void setBreakerNumber(int breakerNumber) {
		this.breakerNumber = breakerNumber;
	}

	public String getTypeValue() {
		return typeValue;
	}

	public void setTypeValue(String typeValue) {
		this.typeValue = typeValue;
	}

}
