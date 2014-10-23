package com.qubaopen.survey.entity.user;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

/**
 * 用户验证码提交
 * @author mars
 */
@Entity
@Table(name = "user_captcha_log")
@Audited
public class UserCaptchaLog extends AbstractBaseEntity<Long>{

	private static final long serialVersionUID = 8384780387343617603L;

	/**
	 * 用户
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	/**
	 * 发送给用户的验证码
	 */
	private String captcha;
	
	/**
	 * 返回验证码的状态 0 成功
	 */
	private String status;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

}
