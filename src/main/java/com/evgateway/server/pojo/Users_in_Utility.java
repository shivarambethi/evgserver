package com.evgateway.server.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "user_in_utility")
public class Users_in_Utility extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String type;
	public String name;
	public Long userId;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "Users_in_Utility [type=" + type + ", name=" + name + ", userId=" + userId + "]";
	}

}
