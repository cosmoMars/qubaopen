package com.qubaopen.doctor.repository.doctor;

import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.doctor.Doctor;
import com.qubaopen.survey.entity.doctor.DoctorCase;

/**
 * Created by mars on 15/3/13.
 */
public interface DoctorCaseRepository extends MyRepository<DoctorCase, Long> {

    int countByDoctor(Doctor doctor);
}
