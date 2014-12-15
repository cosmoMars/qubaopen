package com.qubaopen.survey.controller.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.base.RegionCode;
import com.qubaopen.survey.repository.base.RegionCodeRepository;

@RestController
@RequestMapping("regionCodes")
public class RegionCodeController extends AbstractBaseController<RegionCode, Long> {

	@Autowired
	private RegionCodeRepository regionCodeRepository;

	@Override
	protected MyRepository<RegionCode, Long> getRepository() {
		return regionCodeRepository;
	}

}
