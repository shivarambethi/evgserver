package com.evgateway.server.pojo;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "image")
@JsonIgnoreProperties(ignoreUnknown = true, value = { "sites", "stations" })
public class Image extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String url;
	
	private String fileName;

	private String thumbnail;

	private ImageCategory category;

	private String type;

	private int width;

	private int height;
	
	private long userId;
	private boolean flag;



	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	@JoinColumn(name = "category")
	@ManyToOne(fetch = FetchType.EAGER)
	public ImageCategory getCategory() {
		return category;
	}

	public void setCategory(ImageCategory category) {
		this.category = category;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	
	@Column(name="userId",columnDefinition = "double precision default 0 not null")
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	@Column(name="flag",columnDefinition = "double precision default 0 not null")
	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "Image [url=" + url + ", fileName=" + fileName + ", thumbnail=" + thumbnail + ", category=" + category
				+ ", type=" + type + ", width=" + width + ", height=" + height + ", userId=" + userId + ", flag=" + flag
				+ "]";
	}

	
}
