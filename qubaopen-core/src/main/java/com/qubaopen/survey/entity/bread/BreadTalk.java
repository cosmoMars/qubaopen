package com.qubaopen.survey.entity.bread;

import java.util.Date;

import javax.persistence.Entity;
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
	private String code;

	/**
	 * 使用
	 */
	private boolean used;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
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

}
