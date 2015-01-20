package com.qubaopen.survey.entity.booking;

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
 * @author mars 医师诊断
 */
@Entity
@Table(name = "doctor_diagnosis")
@Audited
public class DoctorDiagnosis extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 5947943274970280987L;

	/**
	 * 订单
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Booking booking;

	/**
	 * 诊断
	 */
	private String diagnosis;

	/**
	 * 时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}
