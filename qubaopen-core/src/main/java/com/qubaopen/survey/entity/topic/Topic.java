package com.qubaopen.survey.entity.topic;

import com.qubaopen.core.entity.AbstractBaseEntity;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 专栏
 */
@Entity
@Table(name = "topic")
@Audited
public class Topic extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 2872417482774254835L;

	/**
	 * 专题名
	 */
	private String name;

	/**
	 * 作者
	 */
	private String author;

	/**
	 * 内容
	 */
//	@Column(name = "cnt", length = 10000)
	@Column(columnDefinition = "TEXT")
	private String content;


	/**
	 * 图片url
	 */
	private String picUrl;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
}
