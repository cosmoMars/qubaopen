package com.qubaopen.doctor.repository.doctor;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.doctor.DoctorIdCardLog;

public interface DoctorIdCardLogRepository extends MyRepository<DoctorIdCardLog, Long> {

	@Query("from DoctorIdCardLog d where month(d.createdDate) = month(now()) and d.doctor = :doctor and d.status in ('0','1','2','local','used')")
	List<DoctorIdCardLog> findCurrentMonthIdCardLogByUser(@Param("doctor") Doctor doctor);
}
