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
 * @author mars
 * 医师自定义事件
 *
 */
@Entity
@Table(name = "booking_self_time")
@Audited
public class BookingSelfTime extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = 4528655091221757658L;

	@ManyToOne(fetch = FetchType.LAZY)
	private Doctor doctor;
	
	/**
	 * 日期
	 */
	@Temporal(TemporalType.DATE)
	private Date date;
	
	/**
	 * 开始时间
	 */
	@Temporal(TemporalType.TIME)
	private Date startTime;
	
	/**
	 * 结束时间
	 */
	@Temporal(TemporalType.TIME)
	private Date endTime;
	
	/**
	 * 地点
	 */
	private String location;
	
	/**
	 * 事情
	 */
	private String content;

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
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
	
}
