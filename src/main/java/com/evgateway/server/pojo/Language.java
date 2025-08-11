package com.evgateway.server.pojo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "language")
@JsonIgnoreProperties(ignoreUnknown = true, value = { "profiles" })
public class Language extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@Column
	private String name;

	@Column
	private String code;
	
	@Column
	private String icon;

	@JsonIgnoreProperties
	private Set<Profile> profiles = new HashSet<Profile>(0);

	public Language() {

	}

	public Language(Long id, String name,String icon) {
		this.id = id;
		this.name = name;
		this.icon=icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "lang")
	public Set<Profile> getProfiles() {
		return this.profiles;
	}

	public void setProfiles(Set<Profile> profiles) {
		this.profiles = profiles;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
	

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "Language [name=" + name + ", code=" + code + ", icon=" + icon + ", profiles=" + profiles + "]";
	}

}