package com.qubaopen.survey.controller.self;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.SelfResultType;
import com.qubaopen.survey.repository.self.SelfResultTypeRepository;

@RestController
@RequestMapping("selfResultTypes")
public class SelfResultTypeController extends AbstractBaseController<SelfResultType, Long> {

	@Autowired
	private SelfResultTypeRepository selfResultTypeRepository;

	@Override
	protected MyRepository<SelfResultType, Long> getRepository() {
		return selfResultTypeRepository;
	}

}
