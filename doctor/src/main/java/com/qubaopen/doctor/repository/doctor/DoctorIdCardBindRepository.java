package com.qubaopen.doctor.repository.doctor;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.doctor.DoctorIdCardBind;
import com.qubaopen.survey.entity.user.UserIDCard;
import com.qubaopen.survey.entity.user.UserIDCardBind;

public interface DoctorIdCardBindRepository extends MyRepository<DoctorIdCardBind, Long> {

	UserIDCardBind findByUserIDCard(UserIDCard userIDCard);
	
	UserIDCard findByDoctor(Doctor doctor);
}
