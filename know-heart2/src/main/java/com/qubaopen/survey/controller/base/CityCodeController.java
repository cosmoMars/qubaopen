package com.qubaopen.survey.controller.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.base.CityCode;
import com.qubaopen.survey.repository.base.CityCodeRepository;

@RestController
@RequestMapping("cityCodes")
public class CityCodeController extends AbstractBaseController<CityCode, Long> {

	@Autowired
	private CityCodeRepository cityCodeRepository;

	@Override
	protected MyRepository<CityCode, Long> getRepository() {
		return cityCodeRepository;
	}

}
