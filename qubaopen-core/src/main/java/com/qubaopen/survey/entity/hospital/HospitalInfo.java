package com.qubaopen.survey.entity.hospital;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

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
	 * 证书
	 */
	private String recordPath;

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

	public String getRecordPath() {
		return recordPath;
	}

	public void setRecordPath(String recordPath) {
		this.recordPath = recordPath;
	}

}
