package com.qubaopen.doctor.repository.doctor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.Doctor;

public interface DoctorRepository extends MyRepository<Doctor, Long> {

	Doctor findByPhone(String phone);

	Doctor findByPhoneAndActivated(String phone, boolean activated);
	
	@Query("from Doctor d join fetch d.doctorInfo left join fetch d.doctorIdCardBind where d.phone = :phone and d.password = :password and d.activated = true")
	Doctor login(@Param("phone") String phone, @Param("password") String password);

}
