package com.qubaopen.survey.controller.self;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.SelfQuestionOption;
import com.qubaopen.survey.repository.self.SelfQuestionOptionRepository;

@RestController
@RequestMapping("selfQuestionOptions")
public class SelfQuestionOptionController extends AbstractBaseController<SelfQuestionOption, Long> {

	@Autowired
	private SelfQuestionOptionRepository selfQuestionOptionRepository;

	@Override
	protected MyRepository<SelfQuestionOption, Long> getRepository() {
		return selfQuestionOptionRepository;
	}

}
