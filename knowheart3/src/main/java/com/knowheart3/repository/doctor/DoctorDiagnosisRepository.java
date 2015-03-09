package com.knowheart3.repository.doctor;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.booking.DoctorDiagnosis;

import java.util.List;

public interface DoctorDiagnosisRepository extends MyRepository<DoctorDiagnosis, Long> {

	List<DoctorDiagnosis> findByBookingIdOrderByTimeDesc(long bookingId);
}
