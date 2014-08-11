package com.qubaopen.survey.entity;

import static javax.persistence.TemporalType.TIMESTAMP;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.envers.Audited;
import org.joda.time.DateTime;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * 广告用户确认表 Created by duel on 2014/6/30.
 */
@Entity
@Table(name = "advertise_confirm")
@Audited
public class AdvertiseConfirm extends AbstractPersistable<Long> {

	private static final long serialVersionUID = -1991942813809871541L;

	/**
	 * 来源
	 */
	private String source;

	/**
	 * idfa
	 */
	private String idfa;

	/**
	 * 是否确认
	 */
	private boolean confirmed;

	/**
	 * 帷千回调地址
	 */
	private String callback;

	/**
	 * ‘点入’验证32位字符
	 */
	private String validate;

	@Temporal(TIMESTAMP)
	private Date createdDate;

	@Temporal(TIMESTAMP)
	private Date lastModifiedDate;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getIdfa() {
		return idfa;
	}

	public void setIdfa(String idfa) {
		this.idfa = idfa;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public String getValidate() {
		return validate;
	}

	public void setValidate(String validate) {
		this.validate = validate;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public DateTime getCreatedDate() {
		return null == createdDate ? null : new DateTime(createdDate);
	}

	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = null == createdDate ? null : createdDate.toDate();
	}

	public DateTime getLastModifiedDate() {
		return null == lastModifiedDate ? null : new DateTime(lastModifiedDate);
	}

	public void setLastModifiedDate(DateTime lastModifiedDate) {
		this.lastModifiedDate = null == lastModifiedDate ? null : lastModifiedDate.toDate();
	}

}
