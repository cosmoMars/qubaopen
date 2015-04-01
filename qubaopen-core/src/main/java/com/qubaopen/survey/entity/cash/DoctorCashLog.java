package com.qubaopen.survey.entity.cash;

import com.qubaopen.core.entity.AbstractBaseEntity;
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.user.User;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.Date;

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
	 * 取现纪录
	 */
	@OneToOne(fetch = FetchType.LAZY)
	private DoctorTakeCash doctorTakeCash;
	
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
	
	/**
	 * 交易状态
	 */
	@Enumerated
	private PayStatus payStatus;
	
	public enum PayStatus {
		Processing, Completed, Failure
	}
	

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

	public DoctorTakeCash getDoctorTakeCash() {
		return doctorTakeCash;
	}

	public void setDoctorTakeCash(DoctorTakeCash doctorTakeCash) {
		this.doctorTakeCash = doctorTakeCash;
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

	public PayStatus getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(PayStatus payStatus) {
		this.payStatus = payStatus;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

}
