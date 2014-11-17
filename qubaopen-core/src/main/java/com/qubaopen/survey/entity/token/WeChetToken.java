package com.qubaopen.survey.entity.token;

import com.qubaopen.core.entity.AbstractBaseEntity;

public class WeChetToken extends AbstractBaseEntity<Long>{

	private static final long serialVersionUID = 8412712594869984482L;

	private String openId;

	private String appId;

	private String appSerect;

	private String token;
	
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

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSerect() {
		return appSerect;
	}

	public void setAppSerect(String appSerect) {
		this.appSerect = appSerect;
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
