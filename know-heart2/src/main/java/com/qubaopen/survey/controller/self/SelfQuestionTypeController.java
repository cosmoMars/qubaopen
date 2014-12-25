package com.qubaopen.survey.controller.self;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.SelfQuestionType;
import com.qubaopen.survey.repository.self.SelfQuestionTypeRepository;

@RestController
@RequestMapping("selfQuestionTypes")
public class SelfQuestionTypeController extends AbstractBaseController<SelfQuestionType, Long> {

	@Autowired
	private SelfQuestionTypeRepository selfQuestionTypeRepository;

	@Override
	protected MyRepository<SelfQuestionType, Long> getRepository() {
		return selfQuestionTypeRepository;
	}

}
