package com.qubaopen.survey.entity.manager;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

/**
 * 后台用户 Created by duel on 2014/6/23.
 */

@Entity
@Table(name = "MANAGER")
@Audited
public class Manager extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 25719655323537319L;

	/**
	 * 用户名
	 */
	@Column(unique = true)
	private String userName;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 真实姓名
	 */
	private String realName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

}
