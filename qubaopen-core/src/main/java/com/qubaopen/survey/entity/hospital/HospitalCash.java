package com.qubaopen.survey.entity.hospital;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity2;

@Entity
@Table(name = "hospital_cash")
@Audited
public class HospitalCash extends AbstractBaseEntity2<Long>{

	private static final long serialVersionUID = -4064479640347886431L;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	private Hospital hospital;

	/**
	 * 总收入
	 */
	private double inCash;

	/**
	 * 总支出
	 */
	private double outCash;

	/**
	 * 当前金额
	 */
	private double currentCash;

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public double getInCash() {
		return inCash;
	}

	public void setInCash(double inCash) {
		this.inCash = inCash;
	}

	public double getOutCash() {
		return outCash;
	}

	public void setOutCash(double outCash) {
		this.outCash = outCash;
	}

	public double getCurrentCash() {
		return currentCash;
	}

	public void setCurrentCash(double currentCash) {
		this.currentCash = currentCash;
	}
	
}
