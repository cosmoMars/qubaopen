package com.qubaopen.survey.controller.self;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.SelfType;
import com.qubaopen.survey.repository.self.SelfTypeRepository;

@RestController
@RequestMapping("selfTypes")
public class selfTypeController extends AbstractBaseController<SelfType, Long> {

	@Autowired
	private SelfTypeRepository selfTypeRepository;

	@Override
	protected MyRepository<SelfType, Long> getRepository() {
		return selfTypeRepository;
	}

}
