package com.qubaopen.backend.vo;

import java.io.Serializable;
import java.util.Date;

public class FavoriteVo implements Serializable {

	private static final long serialVersionUID = 1049102736713668719L;

	private Long id;

	private String name;

	private String content;

	private String author;

	private Date createDate;

	private Date favoriteCreateDate;
	
	private String picUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getFavoriteCreateDate() {
		return favoriteCreateDate;
	}

	public void setFavoriteCreateDate(Date favoriteCreateDate) {
		this.favoriteCreateDate = favoriteCreateDate;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

}
