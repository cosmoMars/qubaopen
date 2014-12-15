package com.qubaopen.survey.controller.survey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.survey.SurveyUserAnswer;
import com.qubaopen.survey.repository.survey.SurveyUserAnswerRepository;

@RestController
@RequestMapping("surveyUserAnswers")
public class SurveyUserAnswerController extends AbstractBaseController<SurveyUserAnswer, Long> {

	@Autowired
	private SurveyUserAnswerRepository surveyUserAnswerRepository;

	@Override
	protected MyRepository<SurveyUserAnswer, Long> getRepository() {
		return surveyUserAnswerRepository;
	}

}
