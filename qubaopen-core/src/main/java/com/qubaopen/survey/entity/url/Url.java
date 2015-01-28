package com.qubaopen.survey.entity.url;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author mars
 * 请求url
 */
@Entity
@Table(name = "url")
public class Url extends AbstractPersistable<Long> {

	private static final long serialVersionUID = 137240764939156644L;
	
	private String requestUrl;

	public String getRequestUrl() {
		return requestUrl;
	}

	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

}
