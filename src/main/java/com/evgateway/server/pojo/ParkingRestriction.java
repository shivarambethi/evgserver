package com.evgateway.server.pojo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "parkingRestriction")
@JsonIgnoreProperties(ignoreUnknown = true, value = { "station" })
public class ParkingRestriction extends BaseEntity{

private static final long serialVersionUID = 1L;
	
	private String name;
	


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
}
