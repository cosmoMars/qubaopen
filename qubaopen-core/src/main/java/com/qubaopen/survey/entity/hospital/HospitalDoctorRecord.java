package com.qubaopen.survey.entity.hospital;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

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
	@JoinColumn(name = "hospital_id")
	private Hospital hospital;

	/**
	 * 医师证明
	 */
	private String doctorRecordPath;

	public Hospital getHospital() {
		return hospital;
	}

	public void setHospital(Hospital hospital) {
		this.hospital = hospital;
	}

	public String getDoctorRecordPath() {
		return doctorRecordPath;
	}

	public void setDoctorRecordPath(String doctorRecordPath) {
		this.doctorRecordPath = doctorRecordPath;
	}

}
