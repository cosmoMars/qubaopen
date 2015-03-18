package com.knowheart3.repository.hospital.custom;

import com.qubaopen.survey.entity.hospital.HospitalInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by mars on 15/3/18.
 */
public interface HospitalInfoRepositoryCustom {
    List<HospitalInfo> findByFilter(Map<String, Object> filters);
}
