package com.evgateway.server.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "promoCode_reward")
public class PromocodeReward extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long userId;
	private double amount;
	private double kWh;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getkWh() {
		return kWh;
	}

	public void setkWh(double kWh) {
		this.kWh = kWh;
	}

	@Override
	public String toString() {
		return "PromocodeReward [userId=" + userId + ", amount=" + amount + ", kWh=" + kWh + "]";
	}

}
