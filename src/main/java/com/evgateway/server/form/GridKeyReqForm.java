package com.evgateway.server.form;

public class GridKeyReqForm {

	/**
	 * 
	 */

	private int noofcards;
	private Long userId;

	// v1

	private String username;
	private Long id;

	private Long noofFOBCards;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private String zipcode;
	private String country;
	private int shippingAddressId;
	private String orgName;
	private String rfidType;
	private String rfid1;
	private String rfid2;
	private String rfid3;
	private String rfid4;
	private String rfid5;

	public String getRfid1() {
		return rfid1;
	}

	public void setRfid1(String rfid1) {
		this.rfid1 = rfid1;
	}

	public String getRfid2() {
		return rfid2;
	}

	public void setRfid2(String rfid2) {
		this.rfid2 = rfid2;
	}

	public String getRfid3() {
		return rfid3;
	}

	public void setRfid3(String rfid3) {
		this.rfid3 = rfid3;
	}

	public String getRfid4() {
		return rfid4;
	}

	public void setRfid4(String rfid4) {
		this.rfid4 = rfid4;
	}

	public String getRfid5() {
		return rfid5;
	}

	public void setRfid5(String rfid5) {
		this.rfid5 = rfid5;
	}

	public int getNoofcards() {
		return noofcards;
	}

	public void setNoofcards(int noofcards) {
		this.noofcards = noofcards;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNoofFOBCards() {
		return noofFOBCards;
	}

	public void setNoofFOBCards(Long noofFOBCards) {
		this.noofFOBCards = noofFOBCards;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getShippingAddressId() {
		return shippingAddressId;
	}

	public void setShippingAddressId(int shippingAddressId) {
		this.shippingAddressId = shippingAddressId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getRfidType() {
		return rfidType;
	}

	public void setRfidType(String rfidType) {
		this.rfidType = rfidType;
	}

	@Override
	public String toString() {
		return "GridKeyReqForm [noofcards=" + noofcards + ", userId=" + userId + ", username=" + username + ", id=" + id
				+ ", noofFOBCards=" + noofFOBCards + ", addressLine1=" + addressLine1 + ", addressLine2=" + addressLine2
				+ ", city=" + city + ", state=" + state + ", zipcode=" + zipcode + ", country=" + country
				+ ", shippingAddressId=" + shippingAddressId + ", orgName=" + orgName + ", rfidType=" + rfidType
				+ ", rfid1=" + rfid1 + ", rfid2=" + rfid2 + ", rfid3=" + rfid3 + ", rfid4=" + rfid4 + ", rfid5=" + rfid5
				+ "]";
	}

}
