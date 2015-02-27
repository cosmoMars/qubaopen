package com.qubaopen.survey.entity.topic;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.self.Self;

/**
 * 每日任务
 */
@Entity
@Table(name = "daily_discovery")
@Audited
public class DailyDiscovery extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -3045517830544998795L;

	/**
	 * 日期
	 */
	@Temporal(TemporalType.DATE)
	private Date time;

	/**
	 * 测评
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Self self;

	/**
	 * 专栏
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Topic topic;

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public Self getSelf() {
		return self;
	}

	public void setSelf(Self self) {
		this.self = self;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

}
