package com.qubaopen.survey.controller.survey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qubaopen.core.controller.AbstractBaseController;
import com.qubaopen.core.repository.MyRepository;
import com.qubaopen.survey.entity.survey.SurveyUserQuestionnaireType;
import com.qubaopen.survey.repository.survey.SurveyUserQuestionnaireTypeRepository;

@RestController
@RequestMapping("surveyUserQuestionnaireTypes")
public class SurveyUserQuestionnaireTypeController extends AbstractBaseController<SurveyUserQuestionnaireType, Long> {

	@Autowired
	private SurveyUserQuestionnaireTypeRepository surveyUserQuestionnaireTypeRepository;

	@Override
	protected MyRepository<SurveyUserQuestionnaireType, Long> getRepository() {
		return surveyUserQuestionnaireTypeRepository;
	}

}
