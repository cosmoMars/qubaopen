package com.knowheart3.repository.booking;

import java.util.Date;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.booking.BookingTime;
import com.qubaopen.survey.entity.doctor.Doctor;

public interface BookingTimeRepository extends MyRepository<BookingTime, Long> {

//	@Query("from DoctorBookingTime dbt where dbt.doctor = :doctor and dbt.startTime >= :time and dbt.endTime <= :time)")
//	List<DoctorBookingTime> findAllByTime(@Param("time") Date time, @Param("doctor") Doctor doctor);
//	
	// and DATE_FORMAT(dbt.endTime,'%Y-%m-%d') = :time
//	@Query("from BookingTime bt where bt.doctor = :doctor and DATE_FORMAT(bt.startTime,'%Y-%m-%d') = :time")
//	List<BookingTime> findAllByTime(@Param("time") String time, @Param("doctor") Doctor doctor);
	
	BookingTime findByDoctorAndTime(Doctor doctor, Date date);
}
