package com.qubaopen.survey.entity.doctor;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

/**
 * @author mars
 * 医师详细预约时间
 */
//@Entity
//@Table(name = "doctor_booking_time")
//@Audited
public class DoctorBookingTime extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 8567417095250318753L;

	@ManyToOne(fetch = FetchType.LAZY)
	private Doctor doctor;

	@Temporal(TemporalType.DATE)
	private Date time;

	private String strTime;

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getStrTime() {
		return strTime;
	}

	public void setStrTime(String strTime) {
		this.strTime = strTime;
	}

}
