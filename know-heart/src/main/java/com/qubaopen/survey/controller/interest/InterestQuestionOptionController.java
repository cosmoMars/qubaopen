package com.qubaopen.survey.controller.interest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.interest.InterestQuestionOption;
import com.qubaopen.survey.repository.interest.InterestQuestionOptionRepository;

@RestController
@RequestMapping("interestQuestionOptions")
public class InterestQuestionOptionController extends AbstractBaseController<InterestQuestionOption, Long> {

	@Autowired
	private InterestQuestionOptionRepository interestQuestionOptionRepository;

	@Override
	protected MyRepository<InterestQuestionOption, Long> getRepository() {
		return interestQuestionOptionRepository;
	}

}
