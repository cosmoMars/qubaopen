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
import com.qubaopen.survey.entity.doctor.Doctor;

/**
 * @author mars 医师详细预约时间
 */
@Entity
@Table(name = "booking_time")
@Audited
public class BookingTime extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 8567417095250318753L;

	@ManyToOne(fetch = FetchType.LAZY)
	private Doctor doctor;

	/**
	 * 提醒时间
	 */
	private int remindTime;

	/**
	 * 日期
	 */
	@Temporal(TemporalType.DATE)
	private Date time;

	/**
	 * 时间模型
	 */
	private String bookingModel;

	/**
	 * 重复模型 1110000
	 */
	private String repeatModel;

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public int getRemindTime() {
		return remindTime;
	}

	public void setRemindTime(int remindTime) {
		this.remindTime = remindTime;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getBookingModel() {
		return bookingModel;
	}

	public void setBookingModel(String bookingModel) {
		this.bookingModel = bookingModel;
	}

	public String getRepeatModel() {
		return repeatModel;
	}

	public void setRepeatModel(String repeatModel) {
		this.repeatModel = repeatModel;
	}

}
