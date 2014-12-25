package com.qubaopen.survey.controller.self;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.SelfResultOption;
import com.qubaopen.survey.repository.self.SelfResultOptionRepository;

@RestController
@RequestMapping("selfResultOptions")
public class SelfResultOptionController extends AbstractBaseController<SelfResultOption, Long> {

	@Autowired
	private SelfResultOptionRepository selfResultOptionRepository;

	@Override
	protected MyRepository<SelfResultOption, Long> getRepository() {
		return selfResultOptionRepository;
	}

}
