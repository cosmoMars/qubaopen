package com.qubaopen.survey.controller.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.base.AreaCode;
import com.qubaopen.survey.repository.base.AreaCodeRepository;

@RestController
@RequestMapping("areaCodes")
public class AreaCodeController extends AbstractBaseController<AreaCode, Long> {

	@Autowired
	private AreaCodeRepository areaCodeRepository;

	@Override
	protected MyRepository<AreaCode, Long> getRepository() {
		return areaCodeRepository;
	}


}
