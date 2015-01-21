package com.qubaopen.survey.entity.bread;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

@Entity
@Table(name = "bread_talk")
@Audited
public class BreadTalk extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 6630282403803507713L;

	/**
	 * 兑换吗
	 */
	@Column(unique = true)
	private String code;

	/**
	 * 金额
	 */
	private int money;

	/**
	 * 使用状态
	 */
	@Enumerated
	private Status status;

	public enum Status {
		Unused, Using, Used
	}

	/**
	 * 领取时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date receiveTime;

	/**
	 * 兑换时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date exchangeTime;

	@Enumerated
	private Level level;

	public enum Level {
		Ten, Twenty, Fifty, Hundred
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getReceiveTime() {
		return receiveTime;
	}

	public void setReceiveTime(Date receiveTime) {
		this.receiveTime = receiveTime;
	}

	public Date getExchangeTime() {
		return exchangeTime;
	}

	public void setExchangeTime(Date exchangeTime) {
		this.exchangeTime = exchangeTime;
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

}
