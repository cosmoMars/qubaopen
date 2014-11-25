package com.qubaopen.survey.entity.doctor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.qubaopen.core.entity.AbstractBaseEntity;

@Entity
@Table(name = "doctor_address")
@Audited
public class DoctorAddress extends AbstractBaseEntity<Long> {

	private static final long serialVersionUID = -1013156369492917523L;

	@ManyToOne(fetch = FetchType.LAZY)
	private Doctor doctor;
	
	private String address;
	
	private boolean used;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

}
