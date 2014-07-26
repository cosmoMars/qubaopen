package com.qubaopen.survey.controller.self;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.self.SelfUserAnswer;
import com.qubaopen.survey.repository.self.SelfUserAnswerRepository;

@RestController
@RequestMapping("selfUserAnswers")
public class SelfUserAnswerController extends AbstractBaseController<SelfUserAnswer, Long> {

	@Autowired
	private SelfUserAnswerRepository selfUserAnswerRepository;

	@Override
	protected MyRepository<SelfUserAnswer, Long> getRepository() {
		return selfUserAnswerRepository;
	}

}
