package com.evgateway.server.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;

public class AutoReload {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;

	@Column(name = "amount", length = 16)
	private double amount;

	@Column(name = "lowBalance", length = 16)
	private double lowBalance;

	private long userId;

	private String cardNo;

	private String paymentId;

	private List<WorldPayCreditCard> cardList = new ArrayList<WorldPayCreditCard>();

	private boolean flag;

	private String cardId;

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getLowBalance() {
		return lowBalance;
	}

	public void setLowBalance(double lowBalance) {
		this.lowBalance = lowBalance;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public List<WorldPayCreditCard> getCardList() {
		return cardList;
	}

	public void setCardList(List<WorldPayCreditCard> cardList) {
		this.cardList = cardList;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	@Override
	public String toString() {
		return "AutoReload [amount=" + amount + ", lowBalance=" + lowBalance + ", userId=" + userId + ", cardNo="
				+ cardNo + ", paymentId=" + paymentId + ", cardList=" + cardList + ", flag=" + flag + ", cardId="
				+ cardId + "]";
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
