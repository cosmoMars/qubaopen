package com.qubaopen.survey.repository.doctor;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.doctor.DoctorBookingTime;

public interface DoctorBookingTimeRepository extends MyRepository<DoctorBookingTime, Long> {

//	@Query("from DoctorBookingTime dbt where dbt.doctor = :doctor and dbt.startTime >= :time and dbt.endTime <= :time)")
//	List<DoctorBookingTime> findAllByTime(@Param("time") Date time, @Param("doctor") Doctor doctor);
//	
	
	@Query("from DoctorBookingTime dbt where dbt.doctor = :doctor and DATE_FORMAT(dbt.startTime,'%Y-%m-%d') = :time and DATE_FORMAT(dbt.endTime,'%Y-%m-%d') = :time)")
	List<DoctorBookingTime> findAllByTime(@Param("time") String time, @Param("doctor") Doctor doctor);
}
