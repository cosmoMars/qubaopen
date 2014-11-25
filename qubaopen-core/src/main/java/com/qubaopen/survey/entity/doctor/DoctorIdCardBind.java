package com.qubaopen.survey.entity.doctor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity2;
import com.qubaopen.survey.entity.user.UserIDCard;

@Entity
@Table(name = "doctor_id_card_bind")
@Audited
public class DoctorIdCardBind extends AbstractBaseEntity2<Long> {

	private static final long serialVersionUID = 1062291863381453506L;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	private Doctor doctor;

	/**
	 * 用户身份证id
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id_card", unique = true, nullable = false)
	private UserIDCard userIDCard;

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public UserIDCard getUserIDCard() {
		return userIDCard;
	}

	public void setUserIDCard(UserIDCard userIDCard) {
		this.userIDCard = userIDCard;
	}

}
