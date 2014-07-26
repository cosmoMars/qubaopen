package com.qubaopen.survey.controller.self

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.Self
import com.qubaopen.survey.repository.self.SelfRepository;

@RestController
@RequestMapping("selfs")
public class SelfController extends AbstractBaseController<Self, Long> {

	@Autowired
	SelfRepository selfRepository

	@Override
	protected MyRepository<Self, Long> getRepository() {
		selfRepository
	}

}
