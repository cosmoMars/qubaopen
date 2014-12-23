package com.qubaopen.doctor.repository.booking;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.booking.BookingSelfTime;
import com.qubaopen.survey.entity.doctor.Doctor;

public interface BookingSelfTimeRepository extends MyRepository<BookingSelfTime, Long> {
	
	int deleteById(long id);
	
	@Query("from BookingSelfTime bst where bst.doctor = :doctor and date(bst.startTime) <= date(:time) and date(bst.endTime) >= date(:time)")
	List<BookingSelfTime> findByDoctorAndTime(@Param("doctor") Doctor doctor, @Param("time") Date time);
}
