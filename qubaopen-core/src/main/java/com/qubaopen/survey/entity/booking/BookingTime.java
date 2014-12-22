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
	 * 日期
	 */
	@Temporal(TemporalType.DATE)
	private Date time;
	
	/**
	 * 时间模型
	 */
	private String bookingModel;
	
	private Type type;
	
	private enum Type {
		Self, Default
	}

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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getRepeatModel() {
		return repeatModel;
	}

	public void setRepeatModel(String repeatModel) {
		this.repeatModel = repeatModel;
	}
	
}
