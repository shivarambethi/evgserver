package com.evgateway.server.pojo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "cardDelHistory")
public class CardDelHistory extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	private String cardNo;
	private String cardType;
	private Long accountId;
	private Date createdDate;
	private String paymentType;
	private String expiryMonth;
	private String PaymentAccountId;
	private String expiryYear;
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getExpiryMonth() {
		return expiryMonth;
	}
	public void setExpiryMonth(String expiryMonth) {
		this.expiryMonth = expiryMonth;
	}
	public String getPaymentAccountId() {
		return PaymentAccountId;
	}
	public void setPaymentAccountId(String paymentAccountId) {
		PaymentAccountId = paymentAccountId;
	}
	public String getExpiryYear() {
		return expiryYear;
	}
	public void setExpiryYear(String expiryYear) {
		this.expiryYear = expiryYear;
	}
	@Override
	public String toString() {
		return "CardDelHistory [cardNo=" + cardNo + ", cardType=" + cardType + ", accountId=" + accountId
				+ ", createdDate=" + createdDate + ", paymentType=" + paymentType + ", expiryMonth=" + expiryMonth
				+ ", PaymentAccountId=" + PaymentAccountId + ", expiryYear=" + expiryYear + "]";
	}
	
	
}
