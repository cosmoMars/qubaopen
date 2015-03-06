package com.qubaopen.survey.entity.topic;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

/**
 * 练习
 */
@Entity
@Table(name = "exercise")
@Audited
public class Exercise extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -4231296994213087799L;
	
	/**
	 * 练习标题
	 */
	private String name;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 图片url
	 */
	private String url;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
