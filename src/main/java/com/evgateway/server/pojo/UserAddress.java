package com.evgateway.server.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_address")
public class UserAddress extends BaseEntityString {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String usersaddresses_type;
	private long users_id;
	private String address1;
	private String address2;
	private String city;
	private String taxterritories_id;
	private String postalcode;

	private double latitude;
	private double longitude;

	private String filter;

	public String getUsersaddresses_type() {
		return usersaddresses_type;
	}

	public void setUsersaddresses_type(String usersaddresses_type) {
		this.usersaddresses_type = usersaddresses_type;
	}

	public long getUsers_id() {
		return users_id;
	}

	public void setUsers_id(long users_id) {
		this.users_id = users_id;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getTaxterritories_id() {
		return taxterritories_id;
	}

	public void setTaxterritories_id(String taxterritories_id) {
		this.taxterritories_id = taxterritories_id;
	}

	public String getPostalcode() {
		return postalcode;
	}

	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

}
