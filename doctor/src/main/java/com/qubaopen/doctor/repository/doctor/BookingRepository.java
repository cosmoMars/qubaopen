package com.qubaopen.doctor.repository.doctor;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.booking.Booking;
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.user.User;

public interface BookingRepository extends MyRepository<Booking, Long> {

	@Query("from Booking b where b.doctor = :doctor and b.status = :status and b.time >= :time and b.id not in (:ids)")
	List<Booking> findBookingList(@Param("doctor") Doctor doctor, @Param("status") Booking.Status status, @Param("time") Date time, @Param("ids") List<Long> ids, Pageable pageable);
	
	@Query("from Booking b where b.doctor = :doctor and b.time >= :time and b.id not in (:ids)")
	List<Booking> findBookingList(@Param("doctor") Doctor doctor, @Param("time") Date time, @Param("ids") List<Long> ids, Pageable pageable);
	
	@Query("from Booking b where b.doctor = :doctor and b.status = :status and b.time >= :time")
	List<Booking> findBookingList(@Param("doctor") Doctor doctor, @Param("status") Booking.Status status, @Param("time") Date time, Pageable pageable);
	
	@Query("from Booking b where b.doctor = :doctor and b.time >= :time")
	List<Booking> findBookingList(@Param("doctor") Doctor doctor, @Param("time") Date time, Pageable pageable);
	
	@Query("from Booking b where b.doctor = :doctor and b.time in (select max(d.time) from Booking d where d.doctor = :doctor and month(d.time) = :month group by dayofmonth(d.time))")
	List<Booking> retrieveBookingByMonth(@Param("doctor") Doctor doctor, @Param("month") int month);
	
	@Query("from Booking b where b.doctor = :doctor and b.user = :user and b.status = :status")
	List<Booking> findByUserAndStatus(@Param("doctor") Doctor doctor, @Param("user") User user, @Param("status") Booking.Status status, Pageable pageable);
	
	@Query("from Booking b where b.doctor = :doctor and b.user = :user and b.status = :status and b.id not in (:ids)")
	List<Booking> findByUserAndStatus(@Param("doctor") Doctor doctor, @Param("user") User user, @Param("status") Booking.Status status, @Param("ids") List<Long> ids, Pageable pageable);
	
	@Query("from Booking b where b.doctor = :doctor and DATE_FORMAT(b.time,'%Y-%m-%d') = :time")
	List<Booking> findAllByTime(@Param("time") String time, @Param("doctor") Doctor doctor);
	
//	@Query("from Booking b")
//	List<Booking> findByDoctorAndTime(@Param("doctor") Doctor doctor, @Param("time") Date time);

}
