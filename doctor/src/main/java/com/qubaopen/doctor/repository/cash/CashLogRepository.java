package com.qubaopen.doctor.repository.cash;

import java.util.List;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.cash.CashLog;
import com.qubaopen.survey.entity.doctor.Doctor;

public interface CashLogRepository extends MyRepository<CashLog, Long> {
	
	List<CashLog> findByDoctorOrderByCreatedDateDesc(Doctor doctor);
}
