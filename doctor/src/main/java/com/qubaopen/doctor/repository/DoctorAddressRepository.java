package com.qubaopen.doctor.repository;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.doctor.DoctorAddress;

public interface DoctorAddressRepository extends MyRepository<DoctorAddress, Long> {

	
	DoctorAddress findByDoctorAndUsed(Doctor doctor, boolean used);
	
}
