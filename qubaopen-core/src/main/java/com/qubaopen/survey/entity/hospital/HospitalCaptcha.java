package com.qubaopen.survey.entity.hospital;

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

@Entity
@Table(name = "hospital_captcha")
@Audited
public class HospitalCaptcha extends AbstractBaseEntity2<Long> {

	private static final long serialVersionUID = -5838944093506526570L;

	/**
	 *  诊所
	 */
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	private Hospital hospital;

	/**
	 * 验证码
	 */
	private String captcha;

	/**
	 * 短信最后发送时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastSentDate;

	/**
	 * 发送次数
	 */
	private int sentNum;

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public Date getLastSentDate() {
		return lastSentDate;
	}

	public void setLastSentDate(Date lastSentDate) {
		this.lastSentDate = lastSentDate;
	}

	public int getSentNum() {
		return sentNum;
	}

	public void setSentNum(int sentNum) {
		this.sentNum = sentNum;
	}

}
