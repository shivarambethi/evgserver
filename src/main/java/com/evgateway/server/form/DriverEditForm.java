package com.evgateway.server.form;

import java.util.Map;

import com.evgateway.server.pojo.BaseEntity;
import com.evgateway.server.pojo.Language;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverEditForm  extends BaseEntity {

	private String firstName;
	private String lastName;
	private String username;
	private Map<String, String> address ;//= new Address();	
	private String oldPassword;
	private String newPassword;
	private String confirmPassword;
	private Long securityQuesId1;
	private Long securityQuesId2;
	private String answer1;
	private String answer2;
	private boolean showPassword;
	private boolean showSecQuestions;
	private Language lang;
	
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Map<String, String> getAddress() {
		return address;
	}

	public void setAddress(Map<String, String> address) {
		this.address = address;
	}
	
	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public Long getSecurityQuesId1() {
		return securityQuesId1;
	}

	public void setSecurityQuesId1(Long securityQuesId1) {
		this.securityQuesId1 = securityQuesId1;
	}

	public Long getSecurityQuesId2() {
		return securityQuesId2;
	}

	public void setSecurityQuesId2(Long securityQuesId2) {
		this.securityQuesId2 = securityQuesId2;
	}

	public String getAnswer1() {
		return answer1;
	}

	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}

	public String getAnswer2() {
		return answer2;
	}

	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}

	public boolean isShowPassword() {
		return showPassword;
	}

	public void setShowPassword(boolean showPassword) {
		this.showPassword = showPassword;
	}

	public boolean isShowSecQuestions() {
		return showSecQuestions;
	}

	public void setShowSecQuestions(boolean showSecQuestions) {
		this.showSecQuestions = showSecQuestions;
	}
	
	public Language getLang() {
		return lang;
	}

	public void setLang(Language lang) {
		this.lang = lang;
	}

	@Override
	public String toString() {
		return "UserEditForm [firstName=" + firstName + ", lastName="
				+ lastName + ", username=" + username + ", address=" + address
				+ ",securityQuesId1=" + securityQuesId1 + ", securityQuesId2="
				+ securityQuesId2 + ", answer1=" + answer1 + ", answer2="
				+ answer2 + ", showPassword=" + showPassword
				+ ", showSecQuestions=" + showSecQuestions + ", lang=" + lang
				+ "]";
	}

	
	
	
}
