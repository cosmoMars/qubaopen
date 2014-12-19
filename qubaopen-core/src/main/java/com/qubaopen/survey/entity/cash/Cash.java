package com.qubaopen.survey.entity.cash;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity2;
import com.qubaopen.survey.entity.doctor.Doctor;

/**
 * @author mars 用户金额历史
 *
 */
@Entity
@Table(name = "cash")
@Audited
public class Cash extends AbstractBaseEntity2<Long> {

	private static final long serialVersionUID = -3161957322103278426L;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	private Doctor doctor;

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

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
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
