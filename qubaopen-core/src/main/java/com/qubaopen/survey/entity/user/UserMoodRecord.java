package com.qubaopen.survey.entity.user;

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

/**
 * @author mars 心情纪录
 */
@Entity
@Table(name = "user_mood_record")
@Audited
public class UserMoodRecord extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -2904210169157325864L;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@Enumerated
	private Status status;

	private enum Status {
		LowToHigh, HighTide, LowTide, HighToLow
	}

	@Temporal(TemporalType.DATE)
	private Date time;

	private double pa;

	private double na;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public double getPa() {
		return pa;
	}

	public void setPa(double pa) {
		this.pa = pa;
	}

	public double getNa() {
		return na;
	}

	public void setNa(double na) {
		this.na = na;
	}

}
