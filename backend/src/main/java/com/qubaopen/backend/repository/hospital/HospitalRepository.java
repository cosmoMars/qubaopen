package com.qubaopen.backend.repository.hospital;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.hospital.Hospital;
import com.qubaopen.survey.entity.hospital.HospitalInfo;

public interface HospitalRepository extends MyRepository<Hospital,Long> {
	@Query("from Hospital d where d.hospitalInfo.loginStatus=:status")
	List<Hospital> getList(Pageable pageable, @Param("status") HospitalInfo.LoginStatus status);
}
