package com.qubaopen.survey.repository.doctor.custom;

import java.util.List;
import java.util.Map;

import com.qubaopen.survey.entity.doctor.DoctorInfo;

public interface DoctorInfoRepositoryCustom {

	List<DoctorInfo> findByFilter(Map<String, Object> filters);
}
