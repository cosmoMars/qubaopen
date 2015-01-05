package com.qubaopen.doctor.repository.cash;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.cash.DoctorCashLog;
import com.qubaopen.survey.entity.doctor.Doctor;

public interface DoctorCashLogRepository extends MyRepository<DoctorCashLog, Long> {
	
	List<DoctorCashLog> findByDoctorOrderByTimeDesc(Doctor doctor, Pageable pageable);
}
