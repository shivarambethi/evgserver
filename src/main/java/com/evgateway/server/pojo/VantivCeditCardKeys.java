package com.evgateway.server.pojo;


import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="vantivCeditCardKeys")
public class VantivCeditCardKeys extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String merchantName;
	
	private String merchantAPIKey;

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantAPIKey() {
		return merchantAPIKey;
	}

	public void setMerchantAPIKey(String merchantAPIKey) {
		this.merchantAPIKey = merchantAPIKey;
	}

	@Override
	public String toString() {
		return "VantivCeditCardKeys [merchantName=" + merchantName + ", merchantAPIKey=" + merchantAPIKey + "]";
	}
	
	
	
	

}
