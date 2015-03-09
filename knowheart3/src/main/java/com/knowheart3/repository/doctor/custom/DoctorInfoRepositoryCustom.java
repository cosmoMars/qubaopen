package com.knowheart3.repository.doctor.custom;

import com.qubaopen.survey.entity.doctor.DoctorInfo;

import java.util.List;
import java.util.Map;

public interface DoctorInfoRepositoryCustom {

	List<DoctorInfo> findByFilter(Map<String, Object> filters);
}
