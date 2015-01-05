package com.qubaopen.doctor.repository.cash;

import java.util.List;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.cash.DoctorCashLog;
import com.qubaopen.survey.entity.doctor.Doctor;

public interface DoctorCashLogRepository extends MyRepository<DoctorCashLog, Long> {
	
	List<DoctorCashLog> findByDoctorOrderByCreatedDateDesc(Doctor doctor);
}
