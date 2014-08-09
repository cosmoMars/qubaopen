package com.qubaopen.survey.entity.log;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.qubaopen.survey.entity.user.User;

/**
 * @author mars 日志 验证码
 */
@Entity
@Table(name = "log_captcha")
@Audited
public class LogCaptcha extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 7802244121190465603L;

	/**
	 * 用户id
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	/**
	 * 输入的验证码
	 */
	private String enteredCaptcha;

	/**
	 * 正确的验证码
	 */
	private String correctCaptcha;

	/**
	 * 创建时间
	 */
	private Date createdDate;

	/**
	 * 验证结果
	 */
	private Boolean verifyResult;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getEnteredCaptcha() {
		return enteredCaptcha;
	}

	public void setEnteredCaptcha(String enteredCaptcha) {
		this.enteredCaptcha = enteredCaptcha;
	}

	public String getCorrectCaptcha() {
		return correctCaptcha;
	}

	public void setCorrectCaptcha(String correctCaptcha) {
		this.correctCaptcha = correctCaptcha;
	}

	public Boolean getVerifyResult() {
		return verifyResult;
	}

	public void setVerifyResult(Boolean verifyResult) {
		this.verifyResult = verifyResult;
	}


}
