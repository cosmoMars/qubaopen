package com.qubaopen.doctor.repository.comment;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.comment.Help;
import com.qubaopen.survey.entity.doctor.Doctor;

public interface HelpRepository extends MyRepository<Help, Long> {

	
	@Query("from Help h where h.id in (select hc.help.id from HelpComment hc where hc.doctor = :doctor)")
	List<Help> findByDoctor(@Param("doctor") Doctor doctor, Pageable pageable);
}
