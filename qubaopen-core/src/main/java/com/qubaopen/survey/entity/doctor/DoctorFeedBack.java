package com.qubaopen.survey.entity.doctor;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

@Entity
@Table(name = "doctor_feed_back")
@Audited
public class DoctorFeedBack extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -3502693697744388631L;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Doctor doctor;

	private String content;

	@Enumerated
	private FeedBackType feedBackType;

	/**
	 * ORDINARY 0 普通用户, ENTERPRISE 1 企业用户
	 */
	public enum FeedBackType {
		ORDINARY, ENTERPRISE
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public FeedBackType getFeedBackType() {
		return feedBackType;
	}

	public void setFeedBackType(FeedBackType feedBackType) {
		this.feedBackType = feedBackType;
	}

}
