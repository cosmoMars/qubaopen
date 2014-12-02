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
import com.qubaopen.survey.entity.user.User;

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
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Booking booking;

	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;

	/**
	 * 地点
	 */
	private String location;

	private String content;

	private int remindTime;

	/**
	 * 重复模型 1,2,3,4,5,6,7
	 */
	private String repeatModel;

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

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getRemindTime() {
		return remindTime;
	}

	public void setRemindTime(int remindTime) {
		this.remindTime = remindTime;
	}

	public String getRepeatModel() {
		return repeatModel;
	}

	public void setRepeatModel(String repeatModel) {
		this.repeatModel = repeatModel;
	}

}
