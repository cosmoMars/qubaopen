package com.knowheart3.repository.doctor;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.Doctor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DoctorRepository extends MyRepository<Doctor, Long> {

	@Query("from Doctor d where d.id not in (:ids)")
	List<Doctor> findOtherDoctor(@Param("ids") List<Long> ids, Pageable pageable);
	
}
