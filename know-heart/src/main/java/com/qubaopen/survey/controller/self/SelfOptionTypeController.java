package com.qubaopen.survey.controller.self;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.SelfOptionType;
import com.qubaopen.survey.repository.self.SelfOptionTypeRepository;

@RestController
@RequestMapping("selfOptionTypes")
public class SelfOptionTypeController extends AbstractBaseController<SelfOptionType, Long> {

	@Autowired
	private SelfOptionTypeRepository selfOptionTypeRepository;

	@Override
	protected MyRepository<SelfOptionType, Long> getRepository() {
		return selfOptionTypeRepository;
	}

}
