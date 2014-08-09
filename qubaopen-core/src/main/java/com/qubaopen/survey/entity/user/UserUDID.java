package com.qubaopen.survey.entity.user;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.springframework.format.annotation.DateTimeFormat;

import com.qubaopen.core.entity.AbstractBaseEntity2;

/**
 * @author mars UUID
 */
@Entity
@Table(name = "user_udid")
@Audited
public class UserUDID extends AbstractBaseEntity2<Long> {

	private static final long serialVersionUID = -2261329595928486895L;

	/**
	 * 用户id
	 */
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	private User user;

	/**
	 * udid
	 */
	private String udid;

	/**
	 * 提醒开始时间
	 */
	@DateTimeFormat(pattern = "HH:mm")
	@Temporal(TemporalType.TIME)
	private Date startTime;

	/**
	 * 提醒结束时间
	 */
	@DateTimeFormat(pattern = "HH:mm")
	@Temporal(TemporalType.TIME)
	private Date endTime;

	/**
	 * 是否推送
	 */
	private Boolean push;

	/**
	 * 是否确认
	 */
	private Boolean confirmed;

	/**
	 * 用户来源
	 */
	private String userSource;

	/**
	 * 过时 设置新alias失败，此alias无效
	 */
	private Boolean outDate;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Boolean getPush() {
		return push;
	}

	public void setPush(Boolean push) {
		this.push = push;
	}

	public Boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

	public String getUserSource() {
		return userSource;
	}

	public void setUserSource(String userSource) {
		this.userSource = userSource;
	}

	public Boolean getOutDate() {
		return outDate;
	}

	public void setOutDate(Boolean outDate) {
		this.outDate = outDate;
	}

}
