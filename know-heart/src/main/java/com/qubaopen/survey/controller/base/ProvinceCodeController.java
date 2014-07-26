package com.qubaopen.survey.controller.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.base.ProvinceCode;
import com.qubaopen.survey.repository.base.ProvinceCodeRepository;

@RestController
@RequestMapping("provinceCodes")
public class ProvinceCodeController extends AbstractBaseController<ProvinceCode, Long> {

	@Autowired
	private ProvinceCodeRepository provinceCodeRepository;

	@Override
	protected MyRepository<ProvinceCode, Long> getRepository() {
		return provinceCodeRepository;
	}

}
