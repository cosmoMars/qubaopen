package com.qubaopen.survey.controller.survey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.survey.SurveyQuestionOption;
import com.qubaopen.survey.repository.survey.SurveyQuestionOptionRepository;

@RestController
@RequestMapping("surveyQuestionOptions")
public class SurveyQuestionOptionController extends AbstractBaseController<SurveyQuestionOption, Long> {

	@Autowired
	private SurveyQuestionOptionRepository surveyQuestionOptionRepository;

	@Override
	protected MyRepository<SurveyQuestionOption, Long> getRepository() {
		return surveyQuestionOptionRepository;
	}

}
