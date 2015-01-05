package com.qubaopen.survey.repository.doctor;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.Doctor;

public interface DoctorRepository extends MyRepository<Doctor, Long> {

	@Query("from Doctor d where d.id not in (:ids)")
	List<Doctor> findOtherDoctor(@Param("ids") List<Long> ids, Pageable pageable);
	
}
