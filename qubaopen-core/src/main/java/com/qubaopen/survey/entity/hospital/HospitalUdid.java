package com.qubaopen.survey.entity.hospital;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity2;

//@Entity
//@Table(name = "hospital_udid")
//@Audited
public class HospitalUdid extends AbstractBaseEntity2<Long> {

	private static final long serialVersionUID = -5435728234108010206L;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	private Hospital hospital;

	/**
	 * udid
	 */
	private String udid;

	/**
	 * idfa
	 */
	private String idfa;

	/**
	 * imei
	 */
	private String imei;

	/**
	 * 提醒开始时间
	 */
	@Temporal(TemporalType.TIME)
	private Date startTime;

	/**
	 * 提醒结束时间
	 */
	@Temporal(TemporalType.TIME)
	private Date endTime;

	/**
	 * 推送
	 */
	private boolean push;

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public String getUdid() {
		return udid;
	}

	public void setUdid(String udid) {
		this.udid = udid;
	}

	public String getIdfa() {
		return idfa;
	}

	public void setIdfa(String idfa) {
		this.idfa = idfa;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
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

	public boolean isPush() {
		return push;
	}

	public void setPush(boolean push) {
		this.push = push;
	}

}
