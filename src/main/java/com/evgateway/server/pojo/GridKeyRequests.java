package com.evgateway.server.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "gridKey_request")
public class GridKeyRequests extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long count;

	@JsonProperty
	private long userId;

	@Temporal(TemporalType.DATE)
	@Column(name = "dateGenerated", length = 10)
	private Date dateGenerated;

	private String orderId;

	private String status;

	private int fobCount;

	private int rfidCount;

	private long orgId;

	private String firstName;
	private String lastName;
	private String email;

	private String mobile;
	private String address;
	private String rfidType;

	@Temporal(TemporalType.DATE)
	@Column(name = "creation_date", length = 10)
	private Date creationDate = new Date();

	@Temporal(TemporalType.DATE)
	@Column(name = "modified_date", length = 10)
	private Date modifiedDate;

	@Column
	private String createdBy;
	@Column
	private String lastModifiedBy;

	private double amount;
	private long accTransId;

	@Column(name = "amount", columnDefinition = "double precision default 0 not null")
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public GridKeyRequests() {
		super();
		// TODO Auto-generated constructor stub
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Date getDateGenerated() {
		return dateGenerated;
	}

	public void setDateGenerated(Date dateGenerated) {
		this.dateGenerated = dateGenerated;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getFobCount() {
		return fobCount;
	}

	public void setFobCount(int fobCount) {
		this.fobCount = fobCount;
	}

	public int getRfidCount() {
		return rfidCount;
	}

	public void setRfidCount(int rfidCount) {
		this.rfidCount = rfidCount;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getRfidType() {
		return rfidType;
	}

	public void setRfidType(String rfidType) {
		this.rfidType = rfidType;
	}

	@Override
	public String toString() {
		return "GridKeyRequests [count=" + count + ", userId=" + userId + ", dateGenerated=" + dateGenerated
				+ ", orderId=" + orderId + ", status=" + status + ", fobCount=" + fobCount + ", rfidCount=" + rfidCount
				+ ", orgId=" + orgId + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", mobile=" + mobile + ", address=" + address + ", rfidType=" + rfidType + ", creationDate="
				+ creationDate + ", modifiedDate=" + modifiedDate + ", createdBy=" + createdBy + ", lastModifiedBy="
				+ lastModifiedBy + ", amount=" + amount + "]";
	}
	@Column(name = "accTransId", columnDefinition = "double precision default 0 not null")
	public long getAccTransId() {
		return accTransId;
	}

	public void setAccTransId(long accTransId) {
		this.accTransId = accTransId;
	}

}
