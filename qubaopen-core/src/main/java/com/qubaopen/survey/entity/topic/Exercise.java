package com.qubaopen.survey.entity.topic;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.AuditStatus;
import com.qubaopen.survey.entity.doctor.Doctor;
import org.hibernate.envers.Audited;

import javax.persistence.*;

/**
 * 练习
 */
@Entity
@Table(name = "exercise")
@Audited
public class Exercise extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -4231296994213087799L;

	@ManyToOne(fetch = FetchType.LAZY)
	private Doctor doctor;
	
	/**
	 * 练习标题
	 */
	private String name;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 图片url
	 */
	private String url;

	/**
	 * 审核状态
	 */
	@Enumerated
	private AuditStatus status;

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public AuditStatus getStatus() {
		return status;
	}

	public void setStatus(AuditStatus status) {
		this.status = status;
	}
}
