package com.qubaopen.survey.entity.system;

import com.qubaopen.core.entity.AbstractBaseEntity;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Date;

/**
 * 系统 软件版本 Created by duel on 2014/6/27.
 */

@Entity
@Table(name = "SYSTEM_VERSION")
@Audited
public class SystemVersion extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -800100967029492805L;

	/**
	 * 版本号
	 */
	private String version;

	/**
	 * 0 安卓 1 ios 2 web
	 */
	@Enumerated
	private Type type;

	/**
	 * 0 安卓 1 ios 2 web
	 */
	public enum Type {
		ANDROID, IOS, WEB
	}

	/**
	 * 下载地址
	 */
	private String downloadUrl;

	/**
	 * 更新内容 说明
	 */
	private String detail;

	/**
	 * md5
	 */
	private String md5Hash;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date publishTime;

	@Enumerated
	private UseObject useObject;
	
	public enum UseObject {
		User, Doctor, kh3
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getMd5Hash() {
		return md5Hash;
	}

	public void setMd5Hash(String md5Hash) {
		this.md5Hash = md5Hash;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public UseObject getUseObject() {
		return useObject;
	}

	public void setUseObject(UseObject useObject) {
		this.useObject = useObject;
	}

}
