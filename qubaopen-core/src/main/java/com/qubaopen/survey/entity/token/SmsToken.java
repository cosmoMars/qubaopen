package com.qubaopen.survey.entity.token;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

@Entity
@Table(name = "sms_token")
@Audited
public class SmsToken extends AbstractBaseEntity<Long>{
	
	private static final long serialVersionUID = 2215767544905001389L;

	/**
	 * 
	 */
	private String openId;
	
	/**
	 * appId
	 */
	private String appId;
	
	/**
	 * app密码
	 */
	private String appSerect;
	
	/**
	 * token
	 */
	private String token;
	
	/**
	 * url
	 */
	private String url;

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getAppId() {
		return appId;
	}

	public String getAppSerect() {
		return appSerect;
	}

	public void setAppSerect(String appSerect) {
		this.appSerect = appSerect;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
