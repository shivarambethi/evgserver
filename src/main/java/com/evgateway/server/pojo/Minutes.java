package com.evgateway.server.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "minutes")
public class Minutes extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long value;

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

}
