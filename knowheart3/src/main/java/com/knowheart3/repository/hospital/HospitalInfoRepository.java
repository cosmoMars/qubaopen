package com.knowheart3.repository.hospital;

import com.knowheart3.repository.hospital.custom.HospitalInfoRepositoryCustom;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.hospital.HospitalInfo;

public interface HospitalInfoRepository extends MyRepository<HospitalInfo, Long>, HospitalInfoRepositoryCustom {

}
