package com.evgateway.server.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "time")
public class Time extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long hour;
	private long min;
	private long sec;

	
	public long getHour() {
		return hour;
	}

	public void setHour(long hour) {
		this.hour = hour;
	}

	public long getMin() {
		return min;
	}

	public void setMin(long min) {
		this.min = min;
	}

	public long getSec() {
		return sec;
	}

	public void setSec(long sec) {
		this.sec = sec;
	}
	public Time(long hour, long min, long sec) {
		super();
		this.hour = hour;
		this.min = min;
		this.sec = sec;
	}


	

	public Time() {
		super();
	}

	@Override
	public String toString() {
		return "Time [hour=" + hour + ", min=" + min + ", sec=" + sec + "]";
	}

}
