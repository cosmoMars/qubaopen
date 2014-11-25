package com.qubaopen.survey.entity.doctor;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.user.User;

@Entity
@Table(name = "doctor_booking")
@Audited
public class DoctorBooking extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 6872015169540197635L;

	@ManyToOne(fetch = FetchType.LAZY)
	private Doctor doctor;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	/**
	 * 求助原因
	 */
	private String helpReason;

	/**
	 * 拒绝原因
	 */
	private String refusalReason;

	private Date time;

	private String quick;

	@Enumerated
	private ConsultType consultType;

	private enum ConsultType {
		Facetoface, Video
	}

	@Enumerated
	private Status status;

	private enum Status {
		Booking, Accept, Refusal, Consulted, Consulting, Next
	}

	private int money;

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getHelpReason() {
		return helpReason;
	}

	public void setHelpReason(String helpReason) {
		this.helpReason = helpReason;
	}

	public String getRefusalReason() {
		return refusalReason;
	}

	public void setRefusalReason(String refusalReason) {
		this.refusalReason = refusalReason;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getQuick() {
		return quick;
	}

	public void setQuick(String quick) {
		this.quick = quick;
	}

	public ConsultType getConsultType() {
		return consultType;
	}

	public void setConsultType(ConsultType consultType) {
		this.consultType = consultType;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

}
