package com.qubaopen.survey.entity.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * sms189 中国短信网回调 Created by duel on 2014/6/30.
 */
@Entity
@Table(name = "sms_call_back")
@Audited
public class SmsCallBack extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -4276646803175758354L;

	/**
	 * 短信 dxid
	 */
	@Column(unique = true)
	private String backId;

	/**
	 * 短信 yzm验证码
	 */
	private String backCode;

	/**
	 * 时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;

	public String getBackId() {
		return backId;
	}

	public void setBackId(String backId) {
		this.backId = backId;
	}

	public String getBackCode() {
		return backCode;
	}

	public void setBackCode(String backCode) {
		this.backCode = backCode;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}
