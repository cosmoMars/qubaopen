package com.qubaopen.survey.entity.topic;

import com.qubaopen.core.entity.AbstractBaseEntity;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Date;

/**
 * 每日发现 图片
 */
@Entity
@Table(name = "daily_discovery_pic")
@Audited
public class DailyDiscoveryPic extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 4807764622221457469L;
	
	/**
	 * 图片说明
	 */
	private String name;
	
	/**
	 * 图片url
	 */
	private String picUrl;
	
	/**
	 * 开始使用的时间
	 */
	@Temporal(TemporalType.DATE)
	@Column(unique = true)
	private Date startTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
}
