package com.qubaopen.survey.entity.doctor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

/**
 * @author mars 验证码日志
 */
@Entity
@Table(name = "doctor_captcha_log")
@Audited
public class DoctorCaptchaLog extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 6744540370557457163L;

	@ManyToOne(fetch = FetchType.LAZY)
	private Doctor doctor;

	/**
	 * 发送的验证码
	 */
	private String captcha;

	/**
	 * 验证码平台返回状态 0 成功
	 */
	private String status;

	/**
	 * 0 客户端发送，1 客户端接收用户验证码
	 */
	private String action;

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
