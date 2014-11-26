package com.qubaopen.doctor.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.doctor.DoctorBooking;
import com.qubaopen.survey.entity.user.User;

public interface DoctorBookingRepository extends MyRepository<DoctorBooking, Long> {

	@Query("from DoctorBooking db where db.doctor = :doctor and db.status = :status and db.time >= :time and db.id not in (:ids)")
	List<DoctorBooking> findDoctorBookingList(@Param("doctor") Doctor doctor, @Param("status") DoctorBooking.Status status, @Param("time") Date time, @Param("ids") List<Long> ids, Pageable pageable);
	
	@Query("from DoctorBooking db where db.doctor = :doctor and db.time >= :time and db.id not in (:ids)")
	List<DoctorBooking> findDoctorBookingList(@Param("doctor") Doctor doctor, @Param("time") Date time, @Param("ids") List<Long> ids, Pageable pageable);
	
	@Query("from DoctorBooking db where db.doctor = :doctor and db.status = :status and db.time >= :time")
	List<DoctorBooking> findDoctorBookingList(@Param("doctor") Doctor doctor, @Param("status") DoctorBooking.Status status, @Param("time") Date time, Pageable pageable);
	
	@Query("from DoctorBooking db where db.doctor = :doctor and db.time >= :time")
	List<DoctorBooking> findDoctorBookingList(@Param("doctor") Doctor doctor, @Param("time") Date time, Pageable pageable);
	
	@Query("from DoctorBooking db where db.doctor = :doctor and db.time in (select max(d.time) from DoctorBooking d where d.doctor = :doctor and month(d.time) = :month group by dayofmonth(d.time))")
	List<DoctorBooking> retrieveBookingByMonth(@Param("doctor") Doctor doctor, @Param("month") int month);
	
	@Query("from DoctorBooking db where db.doctor = :doctor and db.user = :user and db.status = :status")
	List<DoctorBooking> findByUserAndStatus(@Param("doctor") Doctor doctor, @Param("user") User user, @Param("status") DoctorBooking.Status status, Pageable pageable);
	
	@Query("from DoctorBooking db where db.doctor = :doctor and db.user = :user and db.status = :status and db.id not in (:ids)")
	List<DoctorBooking> findByUserAndStatus(@Param("doctor") Doctor doctor, @Param("user") User user, @Param("status") DoctorBooking.Status status, @Param("ids") List<Long> ids, Pageable pageable);
	
}
