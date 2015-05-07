package com.qubaopen.doctor.repository.doctor.custom;

import com.qubaopen.survey.entity.doctor.Doctor;

import java.util.Map;

/**
 * Created by mars on 15/5/7.
 */
public interface BookingRepositoryCustom {

    Map<String, Object> countBooking(Doctor doctor);
}
