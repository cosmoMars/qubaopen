package com.qubaopen.survey.entity.customer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

/**
 * 客户 Created by duel on 2014/6/25.
 */

@Entity
@Table(name = "CUSTOMER")
@Audited
public class Customer extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -3280191672204359205L;

	/**
	 * 邮箱
	 */
	@Column(unique = true)
	private String email;

	/**
	 * 电话
	 */
	@Column(unique = true)
	private String phone;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 是否激活
	 */
	private Boolean activated;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getActivated() {
		return activated;
	}

	public void setActivated(Boolean activated) {
		this.activated = activated;
	}

}
