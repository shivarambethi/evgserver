package com.evgateway.server.form;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.evgateway.server.pojo.Address;
import com.evgateway.server.pojo.BaseEntity;
import com.evgateway.server.pojo.Organization;

public class UserResigtarion extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String username;
	public String password;
	public String confrimPassword;
	public String email;
	public boolean enabled;
	public String gender;
	public String firstName;
	public String lastName;
	public long orgId;
	public long countryId;

	private List<Organization> org;
	public Set<Address> address = new HashSet<Address>();

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfrimPassword() {
		return confrimPassword;
	}

	public void setConfrimPassword(String confrimPassword) {
		this.confrimPassword = confrimPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Set<Address> getAddress() {
		return address;
	}

	public void setAddress(Set<Address> address) {
		this.address = address;
	}

	public List<Organization> getOrg() {
		return org;
	}

	public void setOrg(List<Organization> org) {
		this.org = org;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public long getCountryId() {
		return countryId;
	}

	public void setCountryId(long countryId) {
		this.countryId = countryId;
	}

	@Override
	public String toString() {
		return "UserResigtarion [username=" + username + ", password=" + password + ", confrimPassword="
				+ confrimPassword + ", email=" + email + ", enabled=" + enabled + ", gender=" + gender + ", firstName="
				+ firstName + ", lastName=" + lastName + ", orgId=" + orgId + ", countryId=" + countryId + ", org="
				+ org + ", address=" + address + "]";
	}

}
