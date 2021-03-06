package com.qubaopen.doctor.repository.comment;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.comment.Help;
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.hospital.Hospital;

public interface HelpRepository extends MyRepository<Help, Long> {

	
	@Query("from Help h where h.id in (select hc.help.id from HelpComment hc where hc.doctor = :doctor)")
	List<Help> findByDoctor(@Param("doctor") Doctor doctor, Pageable pageable);
	
	@Query("from Help h where h.id in (select hc.help.id from HelpComment hc where hc.doctor = :doctor and hc.help.id not in (:ids))")
	List<Help> findByDoctor(@Param("doctor") Doctor doctor, @Param("ids") List<Long> ids, Pageable pageable);

	@Query("from Help h where h.id not in (:ids)")
	List<Help> findAllHelp(@Param("ids") List<Long> ids, Pageable pageable);
	
	@Query("from Help h")
	List<Help> findAllByPageable(Pageable pageable);
	
	@Query("from Help h where h.id in (select hc.help.id from HelpComment hc where hc.hospital = :hospital)")
	List<Help> findByHospital(@Param("hospital") Hospital hospital, Pageable pageable);

	@Query("from Help h where h.id in (select hc.help.id from HelpComment hc where hc.id in (:ids))")
	List<Help> findByComment(@Param("ids") List<Long> ids);

	@Query("from Help h where h.id = (select max(he.id) from Help he)")
	Help findCurrentHelp();

}
