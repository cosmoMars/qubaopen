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
	 * 内容
	 */
	@Column(length = 10000)
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
