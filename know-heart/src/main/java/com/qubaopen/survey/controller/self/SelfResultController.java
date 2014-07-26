package com.qubaopen.survey.controller.self;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.SelfResult;
import com.qubaopen.survey.repository.self.SelfResultRepository;

@RestController
@RequestMapping("selfResults")
public class SelfResultController extends AbstractBaseController<SelfResult, Long> {

	@Autowired
	private SelfResultRepository selfResultRepository;

	@Override
	protected MyRepository<SelfResult, Long> getRepository() {
		return selfResultRepository;
	}

}
