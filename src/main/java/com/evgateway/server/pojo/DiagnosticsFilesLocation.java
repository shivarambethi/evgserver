package com.evgateway.server.pojo;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="diagnosticsFilesLocation")
public class DiagnosticsFilesLocation extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	private String manufacturerId ;
	
	private long stationId ;
	
	private Date date ;
	
	private String location ;

	public String getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public long getStationId() {
		return stationId;
	}

	public void setStationId(long stationId) {
		this.stationId = stationId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "DiagnosticsFilesLocation [manufacturerId=" + manufacturerId + ", stationId=" + stationId + ", date="
				+ date + ", location=" + location + "]";
	}
	
    

}
