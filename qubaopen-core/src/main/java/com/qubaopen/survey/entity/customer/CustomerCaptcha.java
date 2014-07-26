package com.qubaopen.survey.entity.customer;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

/**
 * 客户验证码 Created by duel on 2014/6/27.
 */
@Entity
@Table(name = "customer_captcha")
@Audited
public class CustomerCaptcha implements Serializable {

	private static final long serialVersionUID = 1513240828180985079L;

	@Id
	private Long id;

	/**
	 * 客户
	 */
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	private Customer customer;

	/**
	 * 验证码
	 */
	@Column(nullable = false, length = 6)
	private String captcha;

	/**
	 * 最后验证日期
	 */
	private Date lastCheckedDate;

	/**
	 * 验证次数
	 */
	private Integer checkCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Integer getCheckCount() {
		return checkCount;
	}

	public void setCheckCount(Integer checkCount) {
		this.checkCount = checkCount;
	}

	public Date getLastCheckedDate() {
		return lastCheckedDate;
	}

	public void setLastCheckedDate(Date lastCheckedDate) {
		this.lastCheckedDate = lastCheckedDate;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
}
