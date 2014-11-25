package com.qubaopen.survey.entity.doctor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.user.UserLogType;

@Entity
@Table(name = "doctor_log")
@Audited
public class DoctorLog extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -4272815230955840930L;

	/**
	 * 用户
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id")
	private Doctor doctor;

	/**
	 * 用户类型
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_log_type_id")
	private UserLogType userLogType;

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public UserLogType getUserLogType() {
		return userLogType;
	}

	public void setUserLogType(UserLogType userLogType) {
		this.userLogType = userLogType;
	}

}
