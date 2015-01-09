package com.qubaopen.doctor.controller.hospital;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.doctor.repository.hospital.HospitalInfoRepository;
import com.qubaopen.survey.entity.hospital.HospitalInfo;

@RestController
@RequestMapping('hospitalInfo')
@SessionAttributes('currentHospital')
public class HospitalInfoController extends AbstractBaseController<HospitalInfo, Long> {

	@Autowired
	HospitalInfoRepository hospitalInfoRepository
	
	@Override
	MyRepository<HospitalInfo, Long> getRepository() {
		hospitalInfoRepository
	}

	
	@RequestMapping(value = 'modifyHospitalInfo', method = RequestMethod.POST)
	modifyHospitalInfo(@RequestParam(required = false) String name,
		@RequestParam(required = false) String address) {
		
	}
}
