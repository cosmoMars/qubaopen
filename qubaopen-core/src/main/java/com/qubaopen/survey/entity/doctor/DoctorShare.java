package com.qubaopen.survey.entity.doctor;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

@Entity
@Table(name = "doctor_share")
@Audited
public class DoctorShare extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -7167545989721105650L;

	/**
	 * 医师
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id")
	private Doctor doctor;

	@Enumerated
	private ShareTarget shareTarget;

	/**
	 * 分享目标 0新浪微博 1腾讯微博 2微信朋友圈 3 QQ空间 4微信
	 */
	public enum ShareTarget {
		SINA_WEIBO, TENCENT_WEIBO, WECHAT_FRIEND, QQSPACE, WECHAT
	}

	@Enumerated
	private ShareOrigin shareOrigin;

	/**
	 * 分享来源 0分享软件 
	 */
	public enum ShareOrigin {
		SOFTWARE
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public ShareTarget getShareTarget() {
		return shareTarget;
	}

	public void setShareTarget(ShareTarget shareTarget) {
		this.shareTarget = shareTarget;
	}

	public ShareOrigin getShareOrigin() {
		return shareOrigin;
	}

	public void setShareOrigin(ShareOrigin shareOrigin) {
		this.shareOrigin = shareOrigin;
	}

}
