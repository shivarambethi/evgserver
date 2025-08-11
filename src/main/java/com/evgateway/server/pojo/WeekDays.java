package com.evgateway.server.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "weekdays")
public class WeekDays extends BaseEntity {
	
private static final long serialVersionUID = -377652385835180057L;
	
	@Column(name = "weekDay", unique = true, length = 15)
	private String day;

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	@Override
	public String toString() {
		return "WeekDays [day=" + day + "]";
	}

	
	
	

}
