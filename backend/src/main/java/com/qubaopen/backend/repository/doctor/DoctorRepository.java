package com.qubaopen.backend.repository.doctor;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.doctor.DoctorInfo;

public interface DoctorRepository extends MyRepository<Doctor, Long> {
	@Query("from Doctor d where d.doctorInfo.loginStatus=:status")
	List<Doctor> getList(Pageable pageable, @Param("status") DoctorInfo.LoginStatus status);
}
