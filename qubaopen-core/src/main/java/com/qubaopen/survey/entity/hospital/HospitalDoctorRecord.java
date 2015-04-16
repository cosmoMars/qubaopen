package com.qubaopen.survey.entity.hospital;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qubaopen.core.entity.AbstractBaseEntity;

@Entity
@Table(name = "hospital_doctor_record")
@Audited
public class HospitalDoctorRecord extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -4255564153700954761L;

	/**
	 * 医院
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	@JoinColumn(name = "hospital_info_id")
	private HospitalInfo hospitalInfo;

	/**
	 * 医师证明
	 */
	private String doctorRecordPath;

	public HospitalInfo getHospitalInfo() {
		return hospitalInfo;
	}

	public void setHospitalInfo(HospitalInfo hospitalInfo) {
		this.hospitalInfo = hospitalInfo;
	}

	public String getDoctorRecordPath() {
		return doctorRecordPath;
	}

	public void setDoctorRecordPath(String doctorRecordPath) {
		this.doctorRecordPath = doctorRecordPath;
	}

}
