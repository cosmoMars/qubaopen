package com.qubaopen.survey.entity.user;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

/**
 * 用户金币日志 Created by duel on 2014/6/30.
 */
@Entity
@Table(name = "user_gold_log")
@Audited
public class UserGoldLog extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -6216764186964308759L;

	/**
	 * 用户
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * 用户金币日志类型
	 */
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_gold_log_type_id")
	private UserGoldLogType userGoldLogType;

	/**
	 * 正负 0加 1扣
	 */
	@Enumerated
	private Type type;

	/**
	 * ADD 0 加, MINUS 1 扣
	 */
	private enum Type {
		ADD, MINUS
	}

	/**
	 * 变动数额
	 */
	private int amount;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 详情 根据日志类型 详情可能记录不同的信息
	 */
	private String detail;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserGoldLogType getUserGoldLogType() {
		return userGoldLogType;
	}

	public void setUserGoldLogType(UserGoldLogType userGoldLogType) {
		this.userGoldLogType = userGoldLogType;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

}
