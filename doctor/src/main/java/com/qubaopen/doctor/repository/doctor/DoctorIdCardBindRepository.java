package com.qubaopen.doctor.repository.doctor;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.doctor.DoctorIdCardBind;
import com.qubaopen.survey.entity.user.UserIDCard;

public interface DoctorIdCardBindRepository extends MyRepository<DoctorIdCardBind, Long> {

	DoctorIdCardBind findByUserIDCard(UserIDCard userIDCard);
	
//	DoctorIdCardBind findByDoctorId(long doctorId);
}
