package com.qubaopen.survey.entity.enterprise;

import com.qubaopen.core.entity.AbstractBaseEntity;

public class Enterprise extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 1709142018172486242L;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 启用
	 */
	private boolean activated;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

}