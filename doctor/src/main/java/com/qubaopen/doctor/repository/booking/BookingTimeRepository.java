package com.qubaopen.doctor.repository.booking;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.booking.BookingTime;
import com.qubaopen.survey.entity.doctor.Doctor;

public interface BookingTimeRepository extends MyRepository<BookingTime, Long> {

//	@Query("from BookingTime bt where bt.doctor = :doctor and DATE_FORMAT(bt.startTime,'%Y-%m-%d') = :time")
//	List<BookingTime> findAllByTime(@Param("time") String time, @Param("doctor") Doctor doctor);
	
	BookingTime findByTime(Date time);
}
