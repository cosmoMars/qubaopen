package com.qubaopen.survey.entity.user;

import java.util.Date;

import javax.persistence.Column;
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
 * @author mars 验证码
 */
@Entity
@Table(name = "user_captcha")
@Audited
public class UserCaptcha extends AbstractBaseEntity2<Long> {

	private static final long serialVersionUID = 4486244269473959947L;

	/**
	 * 用户
	 */
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	private User user;

	/**
	 * 验证码
	 */
	@Column(nullable = false, length = 6)
	private String captcha;

	/**
	 * 短信最后发送日期
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastSentDate;

	/**
	 * 短信发送次数
	 */
	private int sentNum;

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
