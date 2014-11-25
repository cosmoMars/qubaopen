package com.qubaopen.doctor.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.Doctor;

public interface DoctorRepository extends MyRepository<Doctor, Long> {

	Doctor findByPhone(String phone);

	@Query("from Doctor d join fetch d.doctorInfo left join fetch d.doctorIdCardBind where phone = :phone and password = :password and activated = true")
	Doctor login(@Param("phone") String phone, @Param("password") String password);

}
