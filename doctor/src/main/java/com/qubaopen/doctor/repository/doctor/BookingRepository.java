package com.qubaopen.doctor.repository.doctor;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.booking.Booking;
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.hospital.Hospital;
import com.qubaopen.survey.entity.user.User;

public interface BookingRepository extends MyRepository<Booking, Long> {

	@Query("from Booking b where b.doctor = :doctor and b.status = :status and b.id not in (:ids)")
	List<Booking> findBookingList(@Param("doctor") Doctor doctor, @Param("status") Booking.Status status, @Param("ids") List<Long> ids, Pageable pageable);
	
	@Query("from Booking b where b.doctor = :doctor and b.id not in (:ids)")
	List<Booking> findBookingList(@Param("doctor") Doctor doctor, @Param("ids") List<Long> ids, Pageable pageable);
	
	@Query("from Booking b where b.doctor = :doctor and b.status = :status")
	List<Booking> findBookingList(@Param("doctor") Doctor doctor, @Param("status") Booking.Status status, Pageable pageable);
	
	@Query("from Booking b where b.doctor = :doctor")
	List<Booking> findBookingList(@Param("doctor") Doctor doctor, Pageable pageable);
	
	@Query("from Booking b where b.doctor = :doctor and b.time in (select max(d.time) from Booking d where d.doctor = :doctor and month(d.time) = :month group by dayofmonth(d.time))")
	List<Booking> retrieveBookingByMonth(@Param("doctor") Doctor doctor, @Param("month") int month);
	
	@Query("from Booking b where b.doctor = :doctor and b.user = :user and b.status = :status")
	List<Booking> findByUserAndStatus(@Param("doctor") Doctor doctor, @Param("user") User user, @Param("status") Booking.Status status, Pageable pageable);
	
	@Query("from Booking b where b.doctor = :doctor and b.user = :user and b.status = :status and b.id not in (:ids)")
	List<Booking> findByUserAndStatus(@Param("doctor") Doctor doctor, @Param("user") User user, @Param("status") Booking.Status status, @Param("ids") List<Long> ids, Pageable pageable);
	
	@Query("from Booking b where b.doctor = :doctor and DATE_FORMAT(b.time,'%Y-%m-%d') = :time and b.status in (3,5,7,8)")
	List<Booking> findAllByTimeAndDoctor(@Param("time") String time, @Param("doctor") Doctor doctor);
	
	@Query("from Booking b where b.hospital = :hospital and DATE_FORMAT(b.time,'%Y-%m-%d') = :time and b.status in (3,5,7,8)")
	List<Booking> findAllByTimeAndHospital(@Param("time") String time, @Param("hospital") Hospital hospital);
	
//	@Query("from Booking b")
//	List<Booking> findByDoctorAndTime(@Param("doctor") Doctor doctor, @Param("time") Date time);
	
	@Query("from Booking b where b.doctor = :doctor and DATE_FORMAT(b.time,'%Y-%m-%d') = :time and b.quick = true")
	List<Booking> findAllByTimeAndQuick(@Param("time") String time, @Param("doctor") Doctor doctor);
	
	@Query("from Booking b where b.doctor = :doctor and DATE_FORMAT(b.time,'%Y-%m-%d') = :time and b.quick = true and b.id != :bookingId")
	List<Booking> findAllByTimeAndQuickWithExist(@Param("time") String time, @Param("doctor") Doctor doctor, @Param("bookingId") long bookingId);
	
	@Query("from Booking b where b.doctor = :doctor and DATE_FORMAT(b.time,'%Y-%m-%d %H') = :time and b.quick = :quick and (b.status = 7 or b.status = 8) and b.id != :bookingId")
	List<Booking> findAllByFormatTimeAndQuick(@Param("time") String time, @Param("doctor") Doctor doctor, @Param("quick") boolean quick, @Param("bookingId") long bookingId);

	@Query("from Booking b where date_format(b.time, '%Y-%m-%d %H:%M') >= :startTime and date_format(b.time, '%Y-%m-%d %H:%M') <= :endTime")
	List<Booking> findByTime(@Param("startTime") String startTime, @Param("endTime") String endTime);
	
}
