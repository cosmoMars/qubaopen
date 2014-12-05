package com.qubaopen.doctor.repository.hospital;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.hospital.Hospital;

public interface HospitalRepository extends MyRepository<Hospital, Long> {

	
	@Query("from Hospital h join fetch h.hospitalInfo where h.email = :email and h.password = :pwd")
	Hospital login(@Param("email") String email, @Param("pwd") String pwd);
	
	Hospital findByEmail(String email);
	
	Hospital findByEmailAndActivated(String email, boolean activated);
	
}
