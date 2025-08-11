package com.evgateway.server.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "promoCode_in_codes")
public class PromoCodesInCodes extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long promoId;

	private String promoCode;

	public PromoCodesInCodes() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PromoCodesInCodes(long promoId, String promoCode) {
		super();
		this.promoId = promoId;
		this.promoCode = promoCode;
	}

	public long getPromoId() {
		return promoId;
	}

	public void setPromoId(long promoId) {
		this.promoId = promoId;
	}

	public String getPromoCode() {
		return promoCode;
	}

	public void setPromoCode(String promoCode) {
		this.promoCode = promoCode;
	}

	@Override
	public String toString() {
		return "PromoCodesInCodes [promoId=" + promoId + ", promoCode=" + promoCode + "]";
	}

}
