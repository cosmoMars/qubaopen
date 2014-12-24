package com.qubaopen.survey.repository.booking;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.booking.Booking;
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.user.User;

public interface BookingRepository extends MyRepository<Booking, Long> {
	
	@Query("from Booking b where b.doctor = :doctor and DATE_FORMAT(b.time,'%Y-%m-%d') = :time")
	List<Booking> findAllByTime(@Param("time") String time, @Param("doctor") Doctor doctor);
	
	@Query("from Booking b where id = (select max(bo.id) from Booking bo where bo.user = :user and bo.status = 4)")
	Booking findMaxBooking(@Param("user") User user);
	
	Booking findByTradeNo(String tradeNo);
}
