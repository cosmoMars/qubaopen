package com.qubaopen.survey.entity.hospital;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

@Entity
@Table(name = "hospital_info")
@Audited
public class HospitalInfo extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -4979217651297221804L;

	/**
	 * 医院
	 */
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@PrimaryKeyJoinColumn
	private Hospital hospital;

	/**
	 * 企业名
	 */
	private String name;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 
	 */
	@Temporal(TemporalType.DATE)
	private Date establishTime;

	/**
	 * 预约时间
	 */
	private String bookTime;

	/**
	 * 联系电话
	 */
	private String phone;

	/**
	 * 紧急电话
	 */
	private String urgentPhone;

	/**
	 * qq
	 */
	private String qq;

	/**
	 * 介绍
	 */
	private String introduce;

	/**
	 * 文字咨询
	 */
	private boolean wordsConsult;

	/**
	 * 最低收费
	 */
	private int minCharge;

	/**
	 * 最大收费
	 */
	private int maxCharge;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "hospital", cascade = { CascadeType.REMOVE, CascadeType.PERSIST })
	private Set<HospitalDoctorRecord> hospitalDoctorRecords;

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getEstablishTime() {
		return establishTime;
	}

	public void setEstablishTime(Date establishTime) {
		this.establishTime = establishTime;
	}

	public String getBookTime() {
		return bookTime;
	}

	public void setBookTime(String bookTime) {
		this.bookTime = bookTime;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUrgentPhone() {
		return urgentPhone;
	}

	public void setUrgentPhone(String urgentPhone) {
		this.urgentPhone = urgentPhone;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public boolean isWordsConsult() {
		return wordsConsult;
	}

	public void setWordsConsult(boolean wordsConsult) {
		this.wordsConsult = wordsConsult;
	}

	public int getMinCharge() {
		return minCharge;
	}

	public void setMinCharge(int minCharge) {
		this.minCharge = minCharge;
	}

	public int getMaxCharge() {
		return maxCharge;
	}

	public void setMaxCharge(int maxCharge) {
		this.maxCharge = maxCharge;
	}

	public Set<HospitalDoctorRecord> getHospitalDoctorRecords() {
		return hospitalDoctorRecords;
	}

	public void setHospitalDoctorRecords(Set<HospitalDoctorRecord> hospitalDoctorRecords) {
		this.hospitalDoctorRecords = hospitalDoctorRecords;
	}

}
