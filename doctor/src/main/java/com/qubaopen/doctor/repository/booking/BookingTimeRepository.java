package com.qubaopen.doctor.repository.booking;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.booking.BookingTime;
import com.qubaopen.survey.entity.doctor.Doctor;

public interface BookingTimeRepository extends MyRepository<BookingTime, Long> {

	@Query("from BookingTime bt where bt.doctor = :doctor and DATE_FORMAT(bt.time,'%Y-%m-%d') = :time")
	BookingTime findByFormatTime(@Param("doctor") Doctor doctor, @Param("time") String Time);
	
	BookingTime findByTime(Doctor doctor, String time);
}
