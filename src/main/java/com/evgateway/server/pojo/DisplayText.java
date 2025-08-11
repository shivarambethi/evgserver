package com.evgateway.server.pojo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "displayText")
@JsonIgnoreProperties(ignoreUnknown = true, value = { "sites", "stations", "tariff" })
public class DisplayText extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String language;

	private String text;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "DisplayText [language=" + language + ", text=" + text + "]";
	}

}
