package com.qubaopen.survey.entity.hospital;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.user.UserLogType;

@Entity
@Table(name = "hospital_log")
@Audited
public class HospitalLog extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 5383961689230905000L;

	/**
	 * 用户
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Hospital hospital;

	/**
	 * 用户类型
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private UserLogType userLogType;

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public UserLogType getUserLogType() {
		return userLogType;
	}

	public void setUserLogType(UserLogType userLogType) {
		this.userLogType = userLogType;
	}

}
