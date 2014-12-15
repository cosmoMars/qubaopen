package com.qubaopen.survey.controller.interest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.interest.InterestUserAnswer;
import com.qubaopen.survey.repository.interest.InterestUserAnswerRepository;

@RestController
@RequestMapping("interestUserAnswers")
public class InterestUserAnswerController extends AbstractBaseController<InterestUserAnswer, Long> {

	@Autowired
	private InterestUserAnswerRepository interestUserAnswerRepository;
	
	@Override
	protected MyRepository<InterestUserAnswer, Long> getRepository() {
		return interestUserAnswerRepository;
	}

}
