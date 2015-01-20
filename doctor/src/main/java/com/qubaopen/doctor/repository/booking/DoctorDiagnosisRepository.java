package com.qubaopen.doctor.repository.booking;

import java.util.List;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.booking.DoctorDiagnosis;

public interface DoctorDiagnosisRepository extends MyRepository<DoctorDiagnosis, Long> {

	List<DoctorDiagnosis> findByBookingIdOrderByTimeDesc(long bookingId);
}
