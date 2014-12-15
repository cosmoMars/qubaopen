package com.qubaopen.survey.controller.interest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.interest.InterestQuestionType;
import com.qubaopen.survey.repository.interest.InterestQuestionTypeRepository;

@RestController
@RequestMapping("interestQuestionTypes")
public class InterestQuestionTypeController extends AbstractBaseController<InterestQuestionType, Long> {

	@Autowired
	private InterestQuestionTypeRepository interestQuestionTypeRepository;

	@Override
	protected MyRepository<InterestQuestionType, Long> getRepository() {
		return interestQuestionTypeRepository;
	}

}
