package com.qubaopen.survey.repository.booking;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.booking.BookingSelfTime;
import com.qubaopen.survey.entity.doctor.Doctor;

public interface BookingSelfTimeRepository extends MyRepository<BookingSelfTime, Long> {

	@Query("from BookingSelfTime bst where bst.doctor = :doctor and bst.startTime <= :time and bst.endTime >= :time")
	List<BookingSelfTime> findByDoctorAndTime(@Param("doctor") Doctor doctor, @Param("time") Date time);
}
