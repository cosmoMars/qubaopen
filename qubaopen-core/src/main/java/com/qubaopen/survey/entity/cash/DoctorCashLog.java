package com.qubaopen.survey.entity.cash;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.user.User;

@Entity
@Table(name = "doctor_cash_log")
@Audited
public class DoctorCashLog extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -7926912318594252013L;

	@ManyToOne(fetch = FetchType.LAZY)
	private Doctor doctor;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	/**
	 * 用户姓名
	 */
	private String userName;
	
	/**
	 * 时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	private Date time;

	private double cash;

	/**
	 * 金币变化类型
	 */
	@Enumerated
	private Type type;
	
	public enum Type {
		In, Out
	}
	
	/**
	 * 收入方式
	 */
	private PayType payType;
	
	public enum PayType {
		Alipay, Bank
	}

	private String detail;

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public double getCash() {
		return cash;
	}

	public void setCash(double cash) {
		this.cash = cash;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public PayType getPayType() {
		return payType;
	}

	public void setPayType(PayType payType) {
		this.payType = payType;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}
