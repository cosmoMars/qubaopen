package com.qubaopen.survey.repository.hospital;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.hospital.Hospital;

public interface HospitalRepository extends MyRepository<Hospital, Long> {

	@Query("from Hospital h where h.id not in (:ids)")
	List<Hospital> findOtherHospital(@Param("ids") List<Long> ids);
}
