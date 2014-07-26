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

import com.qubaopen.core.entity.AbstractBaseEntity2;

/**
 * @author mars UUID
 */
@Entity
@Table(name = "user_udid")
@Audited
public class UserUDID extends AbstractBaseEntity2<Long> {

	private static final long serialVersionUID = -8116399075207140241L;

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
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	/**
	 * 提醒结束时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;

	/**
	 * 是否推送
	 */
	private boolean isPush;

	/**
	 * 是否确认
	 */
	private boolean isConfirm;

	/**
	 * 用户来源
	 */
	private String userSource;

	/**
	 * 过时 设置新alias失败，此alias无效
	 */
	private boolean isOutDate;

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

	public String getUserSource() {
		return userSource;
	}

	public void setUserSource(String userSource) {
		this.userSource = userSource;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isPush() {
		return isPush;
	}

	public void setPush(boolean isPush) {
		this.isPush = isPush;
	}

	public boolean isConfirm() {
		return isConfirm;
	}

	public void setConfirm(boolean isConfirm) {
		this.isConfirm = isConfirm;
	}

	public boolean isOutDate() {
		return isOutDate;
	}

	public void setOutDate(boolean isOutDate) {
		this.isOutDate = isOutDate;
	}

	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
	}

}
