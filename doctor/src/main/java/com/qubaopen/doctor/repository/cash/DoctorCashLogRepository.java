package com.qubaopen.doctor.repository.cash;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.cash.DoctorCashLog;
import com.qubaopen.survey.entity.cash.DoctorTakeCash;
import com.qubaopen.survey.entity.doctor.Doctor;

public interface DoctorCashLogRepository extends MyRepository<DoctorCashLog, Long> {
	
	List<DoctorCashLog> findByDoctorOrderByTimeDesc(Doctor doctor, Pageable pageable);
	
	@Query("from DoctorCashLog dcl where dcl.doctor = :doctor and dcl.type = :type order by dcl.time desc")
	List<DoctorCashLog> findByDoctorAndTypeOrderOrderByTimeDesc(@Param("doctor") Doctor doctor, @Param("type") DoctorCashLog.Type type, Pageable pageable);
	
	DoctorCashLog findByDoctorTakeCash(DoctorTakeCash doctorTakeCash);
	
	@Query("from DoctorCashLog dcl where dcl.doctor = :doctor")
	List<DoctorCashLog> findByDoctor(@Param("doctor") Doctor doctor, Pageable pageable);
	
}
