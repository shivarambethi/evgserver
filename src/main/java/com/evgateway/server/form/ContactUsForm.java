package com.evgateway.server.form;

public class ContactUsForm {

	private static final long serialVersionUID = 1L;
	public String subject;
	public String reason;
	public String message;
	public String user;
	public String userid;
	public String issueCreatedBy;
	public String status;
	private String sendCopyToUser;
	public String comment;
	
	// for v1
	private long orgId;
	private String email;

	public String vehicleMake = "";
	public String vehicleModel = "";

	private String referNo;
	
	public String getVehicleMake() {
		return vehicleMake;
	}

	public void setVehicleMake(String vehicleMake) {
		this.vehicleMake = vehicleMake;
	}

	public String getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the message
	 */

	public String getReason() {
		return reason;
	}

	/**
	 * @param message the message to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	public String getSendCopyToUser() {
		return sendCopyToUser;
	}

	public void setSendCopyToUser(String sendCopyToUser) {
		this.sendCopyToUser = sendCopyToUser;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getIssueCreatedBy() {
		return issueCreatedBy;
	}

	public void setIssueCreatedBy(String issueCreatedBy) {
		this.issueCreatedBy = issueCreatedBy;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "ContactUsForm [subject=" + subject + ", reason=" + reason + ", message=" + message + ", user=" + user
				+ ", userid=" + userid + ", issueCreatedBy=" + issueCreatedBy + ", status=" + status
				+ ", sendCopyToUser=" + sendCopyToUser + ", comment=" + comment + ", orgId=" + orgId + ", email="
				+ email + ", vehicleMake=" + vehicleMake + ", vehicleModel=" + vehicleModel + "]";
	}

	public String getReferNo() {
		return referNo;
	}

	public void setReferNo(String referNo) {
		this.referNo = referNo;
	}

	

}
