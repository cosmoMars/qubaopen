package com.qubaopen.survey.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mars
 *	sms189 token
 */
@Entity
@Table(name = "sms_token")
@Audited
public class SmsToken extends AbstractPersistable<Long>{

	private static final long serialVersionUID = 6967959810629815088L;

	/**
	 * token名称
	 */
	private String name;
	
	/**
	 * token密码
	 */
	private String password;
	
	/**
	 * token
	 */
	private String token;
	
	/**
	 * 时间
	 */
	private long time;
	
	/**
	 * appid
	 */
	private String appId;
	
	/**
	 * app密码
	 */
	private String appPassword;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppPassword() {
		return appPassword;
	}

	public void setAppPassword(String appPassword) {
		this.appPassword = appPassword;
	}
	
}
