package com.qubaopen.survey.entity.user;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

/**
 * @author mars 用户日志表
 */
@Entity
@Table(name = "user_log")
@Audited
public class UserLog extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 7154554675839756190L;

	/**
	 * 用户
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * 用户类型
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_log_type_id")
	private UserLogType userLogType;

	/**
	 * 发生时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date Time;

	public UserLogType getUserLogType() {
		return userLogType;
	}

	public void setUserLogType(UserLogType userLogType) {
		this.userLogType = userLogType;
	}

	public Date getTime() {
		return Time;
	}

	public void setTime(Date time) {
		Time = time;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
